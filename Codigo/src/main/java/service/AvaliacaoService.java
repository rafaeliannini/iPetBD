package service;

import dao.AvaliacaoDAO;
import model.Avaliacao;
import spark.Request;
import spark.Response;

public class AvaliacaoService {

    private AvaliacaoDAO avaliacaoDAO;

    public AvaliacaoService() {
        avaliacaoDAO = new AvaliacaoDAO();
        avaliacaoDAO.conectar();
    }

    public Object add(Request request, Response response) {
        int nota = Integer.parseInt(request.queryParams("nota"));
        int id_post = Integer.parseInt(request.queryParams("id_post"));

        Avaliacao avaliacao = new Avaliacao(-1, nota, id_post);
        avaliacaoDAO.inserirAvaliacao(avaliacao);

        response.status(201); // 201 Created
        return "Avaliacao inserida com sucesso!";
    }

    public Object get(Request request, Response response) {
        int id_avaliacao = Integer.parseInt(request.params(":id"));

        Avaliacao avaliacao = avaliacaoDAO.getAvaliacao(id_avaliacao);

        if (avaliacao != null) {
            response.header("Content-Type", "application/xml");
            response.header("Content-Encoding", "UTF-8");

            return "<avaliacao>\n" + 
                    "\t<id_avaliacao>" + avaliacao.getId_avaliacao() + "</id_avaliacao>\n" +
                    "\t<nota>" + avaliacao.getNota() + "</nota>\n" +
                    "\t<id_post>" + avaliacao.getId_post() + "</id_post>\n" +
                    "</avaliacao>\n";
        } else {
            response.status(404); // 404 Not found
            return "Avaliacao " + id_avaliacao + " não encontrada.";
        }
    }

    public Object update(Request request, Response response) {
        int id_avaliacao = Integer.parseInt(request.params(":id"));

        Avaliacao avaliacao = avaliacaoDAO.getAvaliacao(id_avaliacao);

        if (avaliacao != null) {
            avaliacao.setNota(Integer.parseInt(request.queryParams("nota")));
            avaliacao.setId_post(Integer.parseInt(request.queryParams("id_post")));

            avaliacaoDAO.atualizarAvaliacao(avaliacao);

            response.status(200); // 200 OK
            return "Avaliacao atualizada com sucesso!";
        } else {
            response.status(404); // 404 Not found
            return "Avaliacao não encontrada.";
        }
    }

    public Object remove(Request request, Response response) {
        int id_avaliacao = Integer.parseInt(request.params(":id"));

        boolean status = avaliacaoDAO.excluirAvaliacao(id_avaliacao);

        if (status) {
            response.status(200); // 200 OK
            return "Avaliacao removida com sucesso!";
        } else {
            response.status(404); // 404 Not found
            return "Avaliacao não encontrada.";
        }
    }

    public Object getAll(Request request, Response response) {
        StringBuffer returnValue = new StringBuffer("<avaliacoes type=\"array\">");
        for (Avaliacao avaliacao : avaliacaoDAO.getAvaliacoes()) {
            returnValue.append("\n<avaliacao>\n" + 
                    "\t<id_avaliacao>" + avaliacao.getId_avaliacao() + "</id_avaliacao>\n" +
                    "\t<nota>" + avaliacao.getNota() + "</nota>\n" +
                    "\t<id_post>" + avaliacao.getId_post() + "</id_post>\n" +
                    "</avaliacao>\n");
        }
        returnValue.append("</avaliacoes>");
        response.header("Content-Type", "application/xml");
        response.header("Content-Encoding", "UTF-8");
        return returnValue.toString();
    }
}
