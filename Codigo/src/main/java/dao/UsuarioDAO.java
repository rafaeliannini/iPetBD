package dao;

import java.sql.*;
import model.Usuario;

public class UsuarioDAO {
    private Connection conexao;

    public UsuarioDAO() {
        conexao = null;
    }

    public boolean conectar() {
        String driverName = "org.postgresql.Driver";                    
        String serverName = "localhost";
        String mydatabase = "iPet";
        int porta = 5432;
        String url = "jdbc:postgresql://" + serverName + ":" + porta +"/" + mydatabase;
        String username = "postgres";
        String password = "pao123";
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
            String sql = "INSERT INTO usuario (nome, email, senha) VALUES (?, ?, ?)";
            PreparedStatement pst = conexao.prepareStatement(sql);
            pst.setString(1, usuario.getNome());
            pst.setString(2, usuario.getEmail());
            pst.setString(3, usuario.getSenha());
            pst.executeUpdate();
            pst.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }

    public boolean atualizarUsuario(Usuario usuario) {
        boolean status = false;
        try {
            String sql = "UPDATE usuario SET nome = ?, email = ?, senha = ? WHERE id_usuario = ?";
            PreparedStatement pst = conexao.prepareStatement(sql);
            pst.setString(1, usuario.getNome());
            pst.setString(2, usuario.getEmail());
            pst.setString(3, usuario.getSenha());
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
                usuario = new Usuario(rs.getInt("id_usuario"), rs.getString("nome"), rs.getString("email"), rs.getString("senha"));
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
                     rs.getString("email"), rs.getString("senha"));
                 }
              }
              st.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return usuario;
    }
}
