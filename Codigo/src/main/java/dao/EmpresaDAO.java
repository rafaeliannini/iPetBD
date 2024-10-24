package dao;

import java.sql.*;
import model.Empresa;

public class EmpresaDAO {
    private Connection conexao;

    public EmpresaDAO() {
        conexao = null;
    }

    public boolean conectar() {
        String driverName = "org.postgresql.Driver";
        String serverName = "localhost";
        String mydatabase = "iPet";
        int porta = 5432;
        String url = "jdbc:postgresql://" + serverName + ":" + porta + "/" + mydatabase;
        String username = "postgres";
        String password = "pao123";
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
            String sql = "INSERT INTO empresa (nome, email, senha) VALUES (?, ?, ?)";
            PreparedStatement pst = conexao.prepareStatement(sql);
            pst.setString(1, empresa.getNome());
            pst.setString(2, empresa.getEmail());
            pst.setString(3, empresa.getSenha());
            pst.executeUpdate();
            pst.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }


    public boolean atualizarEmpresa(Empresa empresa) {
        boolean status = false;
        try {
            String sql = "UPDATE empresa SET nome = ?, email = ?, senha = ? WHERE id_empresa = ?";
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setString(1, empresa.getNome());
            stmt.setString(2, empresa.getEmail());
            stmt.setString(3, empresa.getSenha());
            stmt.setInt(4, empresa.getId_empresa());

            stmt.executeUpdate();
            stmt.close();
            status = true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar empresa.", e);
        }
        return status;
    }


    public boolean excluirEmpresa(int id_empresa) {
        boolean status = false;
        try {
            Statement st = conexao.createStatement();
            st.executeUpdate("DELETE FROM Empresa WHERE id_empresa = " + id_empresa);
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
            ResultSet rs = st.executeQuery("SELECT * FROM Empresa WHERE id_empresa = " + id_empresa);
            if (rs.next()) {
                empresa = new Empresa(rs.getInt("id_empresa"), rs.getString("nome"), rs.getString("email"), rs.getString("senha"));
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
            if (conexao == null || conexao.isClosed()) {
                throw new SQLException("Conexão ao banco de dados não está aberta.");
            }

            Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = st.executeQuery("SELECT * FROM Empresa");

            if (rs.next()) {
                rs.last();
                int rowCount = rs.getRow();
                empresas = new Empresa[rowCount];

                rs.beforeFirst();

                for (int i = 0; rs.next(); i++) {
                    empresas[i] = new Empresa(
                        rs.getInt("id_empresa"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("senha")
                    );
                }
            }

            rs.close();
            st.close();

        } catch (SQLException e) {
            System.err.println("Erro ao buscar empresas: " + e.getMessage());
            e.printStackTrace();
        }

        return empresas;
    }

}
