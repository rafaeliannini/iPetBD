package dao;

import java.sql.*;
import model.Usuario;
import org.mindrot.jbcrypt.BCrypt;

public class UsuarioDAO {
    private Connection conexao;

    public UsuarioDAO() {
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
            System.err.println("Conexão NÃO efetuada com o postgres -- Driver não encontrado -- " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Conexão NÃO efetuada com o postgres -- " + e.getMessage());
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

    public boolean inserirUsuario(Usuario usuario) {
        boolean status = false;
        try {
            String senhaCriptografada = BCrypt.hashpw(usuario.getSenha(), BCrypt.gensalt());

            String sql = "INSERT INTO usuario (nome, email, senha) VALUES (?, ?, ?)";
            PreparedStatement pst = conexao.prepareStatement(sql);
            pst.setString(1, usuario.getNome());
            pst.setString(2, usuario.getEmail());
            pst.setString(3, senhaCriptografada);
            pst.executeUpdate();
            pst.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }


    public Usuario verificarLogin(String email, String senha) {
        Usuario usuario = null;

        try {
            String sql = "SELECT id_usuario, nome, email, senha FROM usuario WHERE email = ?";
            PreparedStatement pst = conexao.prepareStatement(sql);
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String senhaArmazenada = rs.getString("senha");

                if (BCrypt.checkpw(senha, senhaArmazenada)) {
                    usuario = new Usuario();
                    usuario.setId_usuario(rs.getInt("id_usuario"));
                    usuario.setNome(rs.getString("nome"));
                    usuario.setEmail(rs.getString("email"));
                }
            }

            rs.close();
            pst.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return usuario;
    }

    
    public boolean atualizarUsuario(Usuario usuario) {
        boolean status = false;
        try {
            String sql = "UPDATE usuario SET nome = ?, email = ?, senha = ? WHERE id_usuario = ?";
            PreparedStatement pst = conexao.prepareStatement(sql);

            // Verifica se a senha foi alterada
            String senhaCriptografada;
            if (usuario.getSenha() != null && !usuario.getSenha().isEmpty()) {
                senhaCriptografada = BCrypt.hashpw(usuario.getSenha(), BCrypt.gensalt()); // Criptografa nova senha
            } else {
                // Busca a senha atual do banco, caso não tenha sido fornecida uma nova
                String sqlSenha = "SELECT senha FROM usuario WHERE id_usuario = ?";
                PreparedStatement pstSenha = conexao.prepareStatement(sqlSenha);
                pstSenha.setInt(1, usuario.getId_usuario());
                ResultSet rsSenha = pstSenha.executeQuery();
                if (rsSenha.next()) {
                    senhaCriptografada = rsSenha.getString("senha");
                } else {
                    throw new RuntimeException("Usuário não encontrado.");
                }
                rsSenha.close();
                pstSenha.close();
            }

            // Preenche os parâmetros
            pst.setString(1, usuario.getNome());
            pst.setString(2, usuario.getEmail());
            pst.setString(3, senhaCriptografada);
            pst.setInt(4, usuario.getId_usuario());

            pst.executeUpdate();
            pst.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException("Erro ao atualizar usuário.", u);
        }
        return status;
    }

    public boolean excluirUsuario(int id_usuario) {
        boolean status = false;
        try {  
            Statement st = conexao.createStatement();
            st.executeUpdate("DELETE FROM usuario WHERE id_usuario = " + id_usuario);
            st.close();
            status = true;
        } catch (SQLException u) {  
            throw new RuntimeException(u);
        }
        return status;
    }

    public Usuario getUsuario(int id_usuario) {
        Usuario usuario = null;
        try {
            Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = st.executeQuery("SELECT * FROM usuario WHERE id_usuario = " + id_usuario);
            if (rs.next()) {
                usuario = new Usuario(rs.getInt("id_usuario"), rs.getString("nome"), rs.getString("email"), null);
            }
            st.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return usuario;
    }

    public Usuario[] getUsuarios() {
        Usuario[] usuario = null;

        try {
            Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = st.executeQuery("SELECT * FROM usuario");        
             if(rs.next()){
                 rs.last();
                 usuario = new Usuario[rs.getRow()];
                 rs.beforeFirst();

                 for(int i = 0; rs.next(); i++) {
                     usuario[i] = new Usuario(rs.getInt("id_usuario"), rs.getString("nome"), 
                     rs.getString("email"), null);
                 }
              }
              st.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return usuario;
    }
}
