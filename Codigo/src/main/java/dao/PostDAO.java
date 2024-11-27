package dao;

import java.sql.*;
import model.Post;

public class PostDAO {
    private Connection conexao;
    
    public PostDAO() {
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
            status = (conexao != null);
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
    
    public boolean inserirPost(Post post) {
        boolean status = false;
        try {  
            String sql = "INSERT INTO Post (conteudo, data, imagem, id_usuario, id_local, nota, nome) "
                       + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conexao.prepareStatement(sql);
            pst.setString(1, post.getConteudo());
            pst.setString(2, post.getData());
            pst.setString(3, post.getImagem());
            pst.setInt(4, post.getId_usuario());
            pst.setInt(5, post.getId_local());
            pst.setDouble(6, post.getNota());
            pst.setString(7, post.getNome());
            pst.executeUpdate();
            pst.close();
            status = true;
        } catch (SQLException u) {  
            throw new RuntimeException(u);
        }
        return status;
    }

    public boolean atualizarPost(Post post) {
        boolean status = false;
        try {
            String sql = "UPDATE Post SET conteudo = ?, data = ?, imagem = ?, id_usuario = ?, id_local = ?, nota = ?, nome = ? "
                       + "WHERE id_post = ?";
            PreparedStatement pst = conexao.prepareStatement(sql);
            pst.setString(1, post.getConteudo());
            pst.setString(2, post.getData());
            pst.setString(3, post.getImagem());
            pst.setInt(4, post.getId_usuario());
            pst.setInt(5, post.getId_local());
            pst.setDouble(6, post.getNota());
            pst.setString(7, post.getNome());
            pst.setInt(8, post.getId_post());

            pst.executeUpdate();
            pst.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }

    public boolean excluirPost(int id_post) {
        boolean status = false;
        try {
            Statement st = conexao.createStatement();
            st.executeUpdate("DELETE FROM Post WHERE id_post = " + id_post);
            st.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }

    public Post getPost(int id_post) {
        Post post = null;
        try {
            String sql = "SELECT * FROM Post WHERE id_post = ?";
            PreparedStatement pst = conexao.prepareStatement(sql);
            pst.setInt(1, id_post);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                post = new Post(
                    rs.getInt("id_post"),
                    rs.getString("nome"), // Inclui o campo nome
                    rs.getString("conteudo"),
                    rs.getString("data"),
                    rs.getString("imagem"),
                    rs.getInt("id_usuario"),
                    rs.getInt("id_local"),
                    rs.getDouble("nota")
                );
            }
            pst.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return post;
    }

    public Post[] getPosts() {
        Post[] posts = null;

        try {
            Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = st.executeQuery("SELECT * FROM Post");
            if (rs.next()) {
                rs.last();
                posts = new Post[rs.getRow()];
                rs.beforeFirst();

                for (int i = 0; rs.next(); i++) {
                    posts[i] = new Post(
                        rs.getInt("id_post"),
                        rs.getString("nome"), // Inclui o campo nome
                        rs.getString("conteudo"),
                        rs.getString("data"),
                        rs.getString("imagem"),
                        rs.getInt("id_usuario"),
                        rs.getInt("id_local"),
                        rs.getDouble("nota")
                    );
                }
            }
            st.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return posts;
    }
}

