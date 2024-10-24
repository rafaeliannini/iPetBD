package service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import dao.EmpresaDAO;
import model.Empresa;
import spark.Request;
import spark.Response;

public class EmpresaService {

    private EmpresaDAO empresaDAO;
    private Gson gson = new Gson();

    public EmpresaService() {
        empresaDAO = new EmpresaDAO();
        empresaDAO.conectar();
    }

    public Object add(Request request, Response response) {
        JsonObject data = gson.fromJson(request.body(), JsonObject.class);
        
        String nome = data.get("nome").getAsString();
        String email = data.get("email").getAsString();
        String senha = data.get("senha").getAsString();

        Empresa empresa = new Empresa(-1, nome, email, senha);
        empresaDAO.inserirEmpresa(empresa);

        response.status(201); // 201 Created
        return "Usuário " + nome + " inserido com sucesso!";
    }

    public Object get(Request request, Response response) {
        int id_empresa = Integer.parseInt(request.params(":id"));

        Empresa empresa = empresaDAO.getEmpresa(id_empresa);

        if (empresa != null) {
            response.header("Content-Type", "application/json");
            response.header("Content-Encoding", "UTF-8");

            return gson.toJson(empresa);
        } else {
            response.status(404); // 404 Not Found
            return gson.toJson("Empresa " + id_empresa + " não encontrada.");
        }
    }


    public Object update(Request request, Response response) {
        int id_empresa = Integer.parseInt(request.params(":id"));
        Empresa empresaAtualizada = gson.fromJson(request.body(), Empresa.class);

        Empresa empresaExistente = empresaDAO.getEmpresa(id_empresa);

        if (empresaExistente != null) {
            empresaExistente.setNome(empresaAtualizada.getNome());
            empresaExistente.setEmail(empresaAtualizada.getEmail());
            empresaExistente.setSenha(empresaAtualizada.getSenha());

            empresaDAO.atualizarEmpresa(empresaExistente);
            response.status(200); // Retorna status 200 OK
            return gson.toJson("Empresa atualizada com sucesso.");
        } else {
            response.status(404); // 404 Not Found
            return gson.toJson("Empresa não encontrada.");
        }
    }

    public Object remove(Request request, Response response) {
        int id_empresa = Integer.parseInt(request.params(":id"));

        boolean status = empresaDAO.excluirEmpresa(id_empresa);

        if (status) {
            response.status(200); // 200 OK
            return "Empresa removida com sucesso!";
        } else {
            response.status(404); // 404 Not found
            return "Empresa não encontrada.";
        }
    }

    public Object getAll(Request request, Response response) {
        Empresa[] empresas = empresaDAO.getEmpresas();

        if (empresas != null) {
            response.header("Content-Type", "application/json");
            response.header("Content-Encoding", "UTF-8");

            StringBuilder returnValue = new StringBuilder("[");
            for (int i = 0; i < empresas.length; i++) {
                returnValue.append("{")
                            .append("\"id_empresa\":").append(empresas[i].getId_empresa()).append(",")
                            .append("\"nome\":\"").append(empresas[i].getNome()).append("\",")
                            .append("\"email\":\"").append(empresas[i].getEmail()).append("\",") // Adicione este campo
                            .append("\"senha\":\"").append(empresas[i].getSenha()).append("\"") // Adicione este campo
                            .append("}");
                if (i < empresas.length - 1) {
                    returnValue.append(",");
                }
            }
            returnValue.append("]");
            return returnValue.toString();
        } else {
            response.status(404); // 404 Not Found
            return "Nenhuma empresa encontrada.";
        }
    }


}
