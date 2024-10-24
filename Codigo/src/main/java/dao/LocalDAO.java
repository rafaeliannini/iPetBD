package dao;

import java.sql.*;
import model.Local;

public class LocalDAO {
    private Connection conexao;

    public LocalDAO() {
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

    public boolean inserirLocal(Local local) {
        boolean status = false;
        try {
            String sql = "INSERT INTO Local (nome, descricao, tipo, imagem_url, nota_media, rua, numero, bairro, cep, id_empresa) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conexao.prepareStatement(sql);
            pst.setString(1, local.getNome());
            pst.setString(2, local.getDescricao());
            pst.setString(3, local.getTipo());
            pst.setString(4, local.getImagem_url());
            pst.setFloat(5, local.getNota_media());
            pst.setString(6, local.getRua());
            pst.setString(7, local.getNumero());
            pst.setString(8, local.getBairro());
            pst.setString(9, local.getCep());
            pst.setInt(10, local.getId_empresa());

            pst.executeUpdate();
            pst.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }

    public boolean atualizarLocal(Local local) {
        boolean status = false;
        try {
            String sql = "UPDATE Local SET nome = ?, descricao = ?, tipo = ?, imagem_url = ?, nota_media = ?, rua = ?, numero = ?, bairro = ?, cep = ?, id_empresa = ? WHERE id_local = ?";
            PreparedStatement pst = conexao.prepareStatement(sql);
            pst.setString(1, local.getNome());
            pst.setString(2, local.getDescricao());
            pst.setString(3, local.getTipo());
            pst.setString(4, local.getImagem_url());
            pst.setFloat(5, local.getNota_media());
            pst.setString(6, local.getRua());
            pst.setString(7, local.getNumero());
            pst.setString(8, local.getBairro());
            pst.setString(9, local.getCep());
            pst.setInt(10, local.getId_empresa());
            pst.setInt(11, local.getId_local());

            pst.executeUpdate();
            pst.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }

    public boolean excluirLocal(int id_local) {
        boolean status = false;
        try {
            String sql = "DELETE FROM Local WHERE id_local = ?";
            PreparedStatement pst = conexao.prepareStatement(sql);
            pst.setInt(1, id_local);
            pst.executeUpdate();
            pst.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }

    public Local getLocal(int id_local) {
        Local local = null;
        try {
            String sql = "SELECT * FROM Local WHERE id_local = ?";
            PreparedStatement pst = conexao.prepareStatement(sql);
            pst.setInt(1, id_local);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                local = new Local(rs.getInt("id_local"), rs.getString("nome"), rs.getString("descricao"), rs.getString("tipo"), rs.getString("imagem_url"),
                                  rs.getFloat("nota_media"), rs.getString("rua"), rs.getString("numero"), rs.getString("bairro"), rs.getString("cep"), rs.getInt("id_empresa"));
            }
            pst.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return local;
    }
    
    public Local[] getLocais() {
        Local[] locais = null;

        try {
            Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            ResultSet rs = st.executeQuery("SELECT * FROM Local");
            
            if (rs.next()) {
                rs.last();
                locais = new Local[rs.getRow()];
                
                rs.beforeFirst();

                for (int i = 0; rs.next(); i++) {
                    locais[i] = new Local(
                        rs.getInt("id_local"), 
                        rs.getString("nome"), 
                        rs.getString("descricao"), 
                        rs.getString("tipo"), 
                        rs.getString("imagem_url"),
                        rs.getFloat("nota_media"), 
                        rs.getString("rua"), 
                        rs.getString("numero"), 
                        rs.getString("bairro"), 
                        rs.getString("cep"), 
                        rs.getInt("id_empresa")
                    );
                }
            }
            st.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        
        return locais;
    }

}
