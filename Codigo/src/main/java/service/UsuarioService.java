package service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dao.UsuarioDAO;
import model.Usuario;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UsuarioService {

    private UsuarioDAO usuarioDAO;
    private Gson gson = new Gson();

    public UsuarioService() {
        usuarioDAO = new UsuarioDAO();
        usuarioDAO.conectar();
    }

    public Object add(Request request, Response response) {
        JsonObject data = gson.fromJson(request.body(), JsonObject.class);
        
        String nome = data.get("nome").getAsString();
        String email = data.get("email").getAsString();
        String senha = data.get("senha").getAsString();

        Usuario usuario = new Usuario(-1, nome, email, senha);
        usuarioDAO.inserirUsuario(usuario);

        response.status(201); // 201 Created
        return "Usuário " + nome + " inserido com sucesso!";
    }

    public Object get(Request request, Response response) {
        int id_usuario = Integer.parseInt(request.params(":id"));

        Usuario usuario = usuarioDAO.getUsuario(id_usuario);

        if (usuario != null) {
            response.header("Content-Type", "application/json");
            response.header("Content-Encoding", "UTF-8");

            return gson.toJson(usuario);
        } else {
            response.status(404); // 404 Not Found
            return gson.toJson("Usuário " + id_usuario + " não encontrado.");
        }
    }

    public Object update(Request request, Response response) {
        int id_usuario = Integer.parseInt(request.params(":id"));
        Usuario usuarioAtualizado = gson.fromJson(request.body(), Usuario.class);

        Usuario usuarioExistente = usuarioDAO.getUsuario(id_usuario);

        if (usuarioExistente != null) {
            usuarioExistente.setNome(usuarioAtualizado.getNome());
            usuarioExistente.setEmail(usuarioAtualizado.getEmail());
            usuarioExistente.setSenha(usuarioAtualizado.getSenha());

            usuarioDAO.atualizarUsuario(usuarioExistente);
            response.status(200); // 200 OK
            return gson.toJson("Usuário atualizado com sucesso.");
        } else {
            response.status(404); // 404 Not Found
            return gson.toJson("Usuário não encontrado.");
        }
    }

    public Object remove(Request request, Response response) {
        int id_usuario = Integer.parseInt(request.params(":id"));

        boolean status = usuarioDAO.excluirUsuario(id_usuario);

        if (status) {
            response.status(200); // 200 OK
            return "Usuário removido com sucesso!";
        } else {
            response.status(404); // 404 Not Found
            return gson.toJson("Usuário não encontrado.");
        }
    }

    public Object getAll(Request request, Response response) {
        Usuario[] arrayUsuarios = usuarioDAO.getUsuarios();
        List<Usuario> usuarios = new ArrayList<>(Arrays.asList(arrayUsuarios));

        String usuariosJson = gson.toJson(usuarios);

        response.header("Content-Type", "application/json");
        response.header("Content-Encoding", "UTF-8");

        return usuariosJson;
    }
    
    public Object login(Request request, Response response) {
        JsonObject data = gson.fromJson(request.body(), JsonObject.class);

        String email = data.get("email").getAsString();
        String senha = data.get("senha").getAsString();

        Usuario usuario = usuarioDAO.verificarLogin(email, senha);

        JsonObject resposta = new JsonObject();

        if (usuario != null) {
            resposta.addProperty("sucesso", true);
            resposta.addProperty("mensagem", "Login realizado com sucesso!");
            resposta.addProperty("id", usuario.getId_usuario());
            response.status(200);
        } else {
            resposta.addProperty("sucesso", false);
            resposta.addProperty("mensagem", "Email ou senha inválidos.");
            response.status(401);
        }

        return resposta.toString();
    }


}
