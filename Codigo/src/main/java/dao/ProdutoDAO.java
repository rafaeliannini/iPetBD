package dao;

import java.sql.*;
import model.Produto;

public class ProdutoDAO {
    private Connection conexao;

    public ProdutoDAO() {
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

    public boolean inserirProduto(Produto produto) {
        boolean status = false;
        try {
            String sql = "INSERT INTO Produto (nome, descricao, imagem, preco, link_site, nota_media, id_empresa) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conexao.prepareStatement(sql);
            pst.setString(1, produto.getNome());
            pst.setString(2, produto.getDescricao());
            pst.setString(3, produto.getImagem());
            pst.setDouble(4, produto.getPreco());
            pst.setString(5, produto.getLink_site());
            pst.setFloat(6, produto.getNota_media());
            pst.setInt(7, produto.getId_empresa());
            pst.executeUpdate();
            pst.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }


    public boolean atualizarProduto(Produto produto) {
        boolean status = false;
        try {
            String sql = "UPDATE Produto SET nome = ?, descricao = ?, imagem = ?, preco = ?, link_site = ?, nota_media = ?, id_empresa = ? WHERE id_produto = ?";
            PreparedStatement pst = conexao.prepareStatement(sql);
            pst.setString(1, produto.getNome());
            pst.setString(2, produto.getDescricao());
            pst.setString(3, produto.getImagem());
            pst.setDouble(4, produto.getPreco());
            pst.setString(5, produto.getLink_site());
            pst.setFloat(6, produto.getNota_media());
            pst.setInt(7, produto.getId_empresa());
            pst.setInt(8, produto.getId_produto());

            pst.executeUpdate();
            pst.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException("Erro ao atualizar produto.", u);
        }
        return status;
    }


    public boolean excluirProduto(int id_produto) {
        boolean status = false;
        try {
            Statement st = conexao.createStatement();
            st.executeUpdate("DELETE FROM Produto WHERE id_produto = " + id_produto);
            st.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }

    public Produto getProduto(int id_produto) {
        Produto produto = null;
        try {
            Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = st.executeQuery("SELECT * FROM Produto WHERE id_produto = " + id_produto);
            if (rs.next()) {
                produto = new Produto(rs.getInt("id_produto"), rs.getString("nome"), rs.getString("descricao"), rs.getString("imagem"),
                                      rs.getDouble("preco"), rs.getString("link_site"), rs.getFloat("nota_media"), rs.getInt("id_empresa"));
            }
            st.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return produto;
    }

    public Produto[] getProdutos() {
        Produto[] produtos = null;

        try {
            Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = st.executeQuery("SELECT * FROM Produto");
            if (rs.next()) {
                rs.last();
                produtos = new Produto[rs.getRow()];
                rs.beforeFirst();

                for (int i = 0; rs.next(); i++) {
                    produtos[i] = new Produto(rs.getInt("id_produto"), rs.getString("nome"), rs.getString("descricao"), rs.getString("imagem"),
                                              rs.getDouble("preco"), rs.getString("link_site"), rs.getFloat("nota_media"), rs.getInt("id_empresa"));
                }
            }
            st.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return produtos;
    }
}
