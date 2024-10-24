package dao;

import java.sql.*;
import model.Avaliacao;

public class AvaliacaoDAO {
    private Connection conexao;

    public AvaliacaoDAO() {
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

    public boolean inserirAvaliacao(Avaliacao avaliacao) {
        boolean status = false;
        try {
            Statement st = conexao.createStatement();
            st.executeUpdate("INSERT INTO Avaliacao (nota, id_post) "
                    + "VALUES (" + avaliacao.getNota() + ", " + avaliacao.getId_post() + ");");
            st.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }

    public boolean atualizarAvaliacao(Avaliacao avaliacao) {
        boolean status = false;
        try {
            Statement st = conexao.createStatement();
            String sql = "UPDATE Avaliacao SET nota = " + avaliacao.getNota() + ", id_post = " + avaliacao.getId_post()
                    + " WHERE id_avaliacao = " + avaliacao.getId_avaliacao();
            st.executeUpdate(sql);
            st.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }

    public boolean excluirAvaliacao(int id_avaliacao) {
        boolean status = false;
        try {
            Statement st = conexao.createStatement();
            st.executeUpdate("DELETE FROM Avaliacao WHERE id_avaliacao = " + id_avaliacao);
            st.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }

    public Avaliacao getAvaliacao(int id_avaliacao) {
        Avaliacao avaliacao = null;
        try {
            Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = st.executeQuery("SELECT * FROM Avaliacao WHERE id_avaliacao = " + id_avaliacao);
            if (rs.next()) {
                avaliacao = new Avaliacao(rs.getInt("id_avaliacao"), rs.getInt("nota"), rs.getInt("id_post"));
            }
            st.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return avaliacao;
    }

    public Avaliacao[] getAvaliacoes() {
        Avaliacao[] avaliacoes = null;

        try {
            Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = st.executeQuery("SELECT * FROM Avaliacao");
            if (rs.next()) {
                rs.last();
                avaliacoes = new Avaliacao[rs.getRow()];
                rs.beforeFirst();

                for (int i = 0; rs.next(); i++) {
                    avaliacoes[i] = new Avaliacao(rs.getInt("id_avaliacao"), rs.getInt("nota"), rs.getInt("id_post"));
                }
            }
            st.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return avaliacoes;
    }
}
