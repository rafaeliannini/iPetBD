package service;

import dao.LocalDAO;
import model.Local;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class LocalService {

    private LocalDAO localDAO;
    private Gson gson = new Gson();

    public LocalService() {
        localDAO = new LocalDAO();
        localDAO.conectar();
    }

    public Object add(Request request, Response response) {
        JsonObject data = gson.fromJson(request.body(), JsonObject.class);

        String nome = data.get("nome").getAsString();
        String descricao = data.get("descricao").getAsString();
        String tipo = data.get("tipo").getAsString();
        String imagem_url = data.get("imagem_url").getAsString();
        float nota_media = data.get("nota_media").getAsFloat();
        String rua = data.get("rua").getAsString();
        String numero = data.get("numero").getAsString();
        String bairro = data.get("bairro").getAsString();
        String cep = data.get("cep").getAsString();
        int id_empresa = data.get("id_empresa").getAsInt();

        Local local = new Local(-1, nome, descricao, tipo, imagem_url, nota_media, rua, numero, bairro, cep, id_empresa);
        localDAO.inserirLocal(local);

        response.status(201); // HTTP 201 Created
        return "Local " + nome + " inserido com sucesso!";
    }

    public Object get(Request request, Response response) {
        int id_local = Integer.parseInt(request.params(":id"));
        Local local = localDAO.getLocal(id_local);

        if (local != null) {
            response.header("Content-Type", "application/json");
            response.header("Content-Encoding", "UTF-8");
            return gson.toJson(local);
        } else {
            response.status(404); // HTTP 404 Not found
            return gson.toJson("Local " + id_local + " não encontrado.");
        }
    }

    public Object update(Request request, Response response) {
        int id_local = Integer.parseInt(request.params(":id"));
        JsonObject data = gson.fromJson(request.body(), JsonObject.class);

        Local localExistente = localDAO.getLocal(id_local);

        if (localExistente != null) {
            String nome = data.has("nome") ? data.get("nome").getAsString().trim() : localExistente.getNome();
            String descricao = data.has("descricao") ? data.get("descricao").getAsString().trim() : localExistente.getDescricao();
            String tipo = data.has("tipo") ? data.get("tipo").getAsString().trim() : localExistente.getTipo();
            String imagem_url = data.has("imagem_url") ? data.get("imagem_url").getAsString().trim() : localExistente.getImagem_url();
            float nota_media = data.has("nota_media") ? data.get("nota_media").getAsFloat() : localExistente.getNota_media();
            String rua = data.has("rua") ? data.get("rua").getAsString().trim() : localExistente.getRua();
            String numero = data.has("numero") ? data.get("numero").getAsString().trim() : localExistente.getNumero();
            String bairro = data.has("bairro") ? data.get("bairro").getAsString().trim() : localExistente.getBairro();
            String cep = data.has("cep") ? data.get("cep").getAsString().trim() : localExistente.getCep();
            int idEmpresa = data.has("id_empresa") ? data.get("id_empresa").getAsInt() : localExistente.getId_empresa();

            localExistente.setNome(nome);
            localExistente.setDescricao(descricao);
            localExistente.setTipo(tipo);
            localExistente.setImagem_url(imagem_url);
            localExistente.setNota_media(nota_media);
            localExistente.setRua(rua);
            localExistente.setNumero(numero);
            localExistente.setBairro(bairro);
            localExistente.setCep(cep);
            localExistente.setId_empresa(idEmpresa);

            localDAO.atualizarLocal(localExistente);

            response.status(200); // HTTP 200 OK
            return gson.toJson("Local atualizado com sucesso.");
        } else {
            response.status(404); // HTTP 404 Not found
            return gson.toJson("Local não encontrado.");
        }
    }

    public Object remove(Request request, Response response) {
        int id_local = Integer.parseInt(request.params(":id"));

        boolean status = localDAO.excluirLocal(id_local);

        if (status) {
            response.status(200); // HTTP 200 OK
            return gson.toJson("Local removido com sucesso.");
        } else {
            response.status(404); // HTTP 404 Not found
            return gson.toJson("Local não encontrado.");
        }
    }

    public Object getAll(Request request, Response response) {
        Local[] arrayLocais = localDAO.getLocais();
        List<Local> locais = new ArrayList<>(Arrays.asList(arrayLocais));
        
        response.header("Content-Type", "application/json");
        response.header("Content-Encoding", "UTF-8");

        return gson.toJson(locais);
    }
}
