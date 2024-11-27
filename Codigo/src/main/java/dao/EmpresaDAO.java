package dao;

import java.sql.*;
import model.Empresa;
import org.mindrot.jbcrypt.BCrypt;

public class EmpresaDAO {
    private Connection conexao;

    public EmpresaDAO() {
        conexao = null;
    }

    public boolean conectar() {
        String driverName = "org.postgresql.Driver";
        String serverName = "banco-dados-ipet.postgres.database.azure.com";
        String mydatabase = "iPet";
        int porta = 5432;
        String url = "jdbc:postgresql://" + serverName + ":" + porta + "/" + mydatabase;
        String username = "iPetAdministrador";
        String password = "BancoDeDados123*";
        boolean status = false;

        try {
            Class.forName(driverName);
            conexao = DriverManager.getConnection(url, username, password);
            status = (conexao == null);
        } catch (ClassNotFoundException e) {
            System.err.println("Erro no driver: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erro na conexão: " + e.getMessage());
        }

        return status;
    }

    public boolean close() {
        boolean status = false;

        try {
            conexao.close();
            status = true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return status;
    }

    public boolean inserirEmpresa(Empresa empresa) {
        boolean status = false;
        try {
            String senhaCriptografada = BCrypt.hashpw(empresa.getSenha(), BCrypt.gensalt());

            String sql = "INSERT INTO empresa (nome, email, senha) VALUES (?, ?, ?)";
            PreparedStatement pst = conexao.prepareStatement(sql);
            pst.setString(1, empresa.getNome());
            pst.setString(2, empresa.getEmail());
            pst.setString(3, senhaCriptografada);
            pst.executeUpdate();
            pst.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }

    public Empresa verificarLogin(String email, String senha) {
        Empresa empresa = null;

        try {
            String sql = "SELECT id_empresa, nome, email, senha FROM empresa WHERE email = ?";
            PreparedStatement pst = conexao.prepareStatement(sql);
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String senhaArmazenada = rs.getString("senha");

                if (BCrypt.checkpw(senha, senhaArmazenada)) {
                    empresa = new Empresa();
                    empresa.setId_empresa(rs.getInt("id_empresa"));
                    empresa.setNome(rs.getString("nome"));
                    empresa.setEmail(rs.getString("email"));
                }
            }

            rs.close();
            pst.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return empresa;
    }

    public boolean atualizarEmpresa(Empresa empresa) {
        boolean status = false;
        try {
            String sql = "UPDATE empresa SET nome = ?, email = ?, senha = ? WHERE id_empresa = ?";
            PreparedStatement pst = conexao.prepareStatement(sql);

            // Verifica se a senha foi alterada
            String senhaCriptografada;
            if (empresa.getSenha() != null && !empresa.getSenha().isEmpty()) {
                senhaCriptografada = BCrypt.hashpw(empresa.getSenha(), BCrypt.gensalt()); // Criptografa nova senha
            } else {
                // Busca a senha atual do banco, caso não tenha sido fornecida uma nova
                String sqlSenha = "SELECT senha FROM empresa WHERE id_empresa = ?";
                PreparedStatement pstSenha = conexao.prepareStatement(sqlSenha);
                pstSenha.setInt(1, empresa.getId_empresa());
                ResultSet rsSenha = pstSenha.executeQuery();
                if (rsSenha.next()) {
                    senhaCriptografada = rsSenha.getString("senha");
                } else {
                    throw new RuntimeException("Empresa não encontrada.");
                }
                rsSenha.close();
                pstSenha.close();
            }

            // Preenche os parâmetros
            pst.setString(1, empresa.getNome());
            pst.setString(2, empresa.getEmail());
            pst.setString(3, senhaCriptografada);
            pst.setInt(4, empresa.getId_empresa());

            pst.executeUpdate();
            pst.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException("Erro ao atualizar empresa.", u);
        }
        return status;
    }

    public boolean excluirEmpresa(int id_empresa) {
        boolean status = false;
        try {
            Statement st = conexao.createStatement();
            st.executeUpdate("DELETE FROM empresa WHERE id_empresa = " + id_empresa);
            st.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }

    public Empresa getEmpresa(int id_empresa) {
        Empresa empresa = null;
        try {
            Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = st.executeQuery("SELECT * FROM empresa WHERE id_empresa = " + id_empresa);
            if (rs.next()) {
                empresa = new Empresa(rs.getInt("id_empresa"), rs.getString("nome"), rs.getString("email"), null);
            }
            st.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return empresa;
    }

    public Empresa[] getEmpresas() {
        Empresa[] empresas = null;

        try {
            Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = st.executeQuery("SELECT * FROM empresa");

            if (rs.next()) {
                rs.last();
                empresas = new Empresa[rs.getRow()];
                rs.beforeFirst();

                for (int i = 0; rs.next(); i++) {
                    empresas[i] = new Empresa(
                        rs.getInt("id_empresa"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        null
                    );
                }
            }
            st.close();
        } catch (SQLException e) {
            System.err.println("Erro ao buscar empresas: " + e.getMessage());
        }

        return empresas;
    }
}
