document.addEventListener('DOMContentLoaded', async function() {
    const usuarioId = localStorage.getItem('usuarioId');
    const tipoCadastro = localStorage.getItem('tipoCadastro');

    if (!usuarioId || !tipoCadastro) {
        alert('Você precisa estar logado para acessar esta página.');
        window.location.href = 'loginForm.html';
        return;
    }

    const url = tipoCadastro === 'empresa' ? `/empresa/${usuarioId}` : `/usuario/${usuarioId}`;

    try {
        const response = await fetch(`https://ipetbh.azurewebsites.net${url}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            const usuario = await response.json();
            document.getElementById('nomeUsuarioInput').value = usuario.nome;
            document.getElementById('emailUsuarioInput').value = usuario.email;
            document.getElementById('senhaUsuario').value = usuario.senha;

            if (tipoCadastro === 'empresa') {
                document.getElementById('painelButton').style.display = 'block';
            } else if(tipoCadastro === 'usuario'){
                document.getElementById('usuarioButton').style.display = 'block';
            }
        } else {
            alert('Erro ao carregar os dados do usuário.');
        }
    } catch (error) {
        console.error('Erro:', error);
        alert('Erro ao carregar os dados do usuário.');
    }

    // Função para atualizar os dados do usuário
    document.getElementById('formPerfil').addEventListener('submit', async function(event) {
        event.preventDefault();

        const nomeAtualizado = document.getElementById('nomeUsuarioInput').value;
        const emailAtualizado = document.getElementById('emailUsuarioInput').value;
        const senhaAtualizada = document.getElementById('senhaUsuario').value;

        const dadosAtualizados = {
            nome: nomeAtualizado,
            email: emailAtualizado,
            senha: senhaAtualizada
        };

        try {
            const response = await fetch(`https://ipetbh.azurewebsites.net${url}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(dadosAtualizados)
            });

            if (response.ok) {
                alert('Dados atualizados com sucesso!');
            } else {
                alert('Erro ao atualizar os dados.');
            }
        } catch (error) {
            console.error('Erro:', error);
            alert('Erro ao atualizar os dados.');
        }
    });
});

function logout() {
    localStorage.removeItem('usuarioId');
    localStorage.removeItem('tipoCadastro');
    alert('Logout realizado com sucesso!');
    window.location.href = 'loginForm.html';
}

function irParaPainel() {
    window.location.href = '../../admin/admin.html';
}

function irParaUsuario() {
    window.location.href = '../../admin/usuario.html';
}
