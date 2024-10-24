async function cadastrarUsuario(event) {
    event.preventDefault();

    const nomeUsuario = document.getElementById('nomeUsuario').value;
    const emailUsuario = document.getElementById('emailUsuario').value;
    const senhaUsuario = document.getElementById('senhaUsuario').value;
    const tipoCadastro = document.querySelector('input[name="tipoCadastro"]:checked').value;

    const url = tipoCadastro === 'empresa' ? '/empresa' : '/usuario';

    const data = {
        nome: nomeUsuario,
        email: emailUsuario,
        senha: senhaUsuario
    };

    try {
        const response = await fetch(`http://localhost:6789${url}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        if (response.ok) {
            var myModal = new bootstrap.Modal(document.getElementById('successModal'));
            myModal.show();
            document.getElementById('formCadastro').reset();
        } else {
            alert('Erro ao cadastrar usuário.');
        }
    } catch (error) {
        console.error('Erro:', error);
        alert('Ocorreu um erro ao tentar realizar o cadastro.');
    }
}

async function login(event) {
    event.preventDefault();
    const emailUsuario = document.getElementById('emailLogin').value;
    const senhaLogin = document.getElementById('senhaLogin').value;
    const tipoCadastro = document.querySelector('input[name="tipoCadastro"]:checked').value;

    const url = tipoCadastro === 'empresa' ? '/empresas' : '/usuarios';

    try {
        const response = await fetch(`http://localhost:6789${url}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error('Erro na resposta da API');
        }

        const dados = await response.json();
        console.log(dados);

        let usuarioEncontrado;

        if (tipoCadastro === 'empresa') {
            usuarioEncontrado = dados.find(empresa => 
                empresa.email === emailUsuario && 
                empresa.senha === senhaLogin
            );
        } else {
            usuarioEncontrado = dados.find(usuario => 
                usuario.email === emailUsuario && 
                usuario.senha === senhaLogin
            );
        }

        if (usuarioEncontrado) {
            localStorage.setItem('usuarioId', tipoCadastro === 'empresa' ? usuarioEncontrado.id_empresa : usuarioEncontrado.id_usuario);
            localStorage.setItem('tipoCadastro', tipoCadastro);
            alert('Login realizado com sucesso!');
            window.location.href = 'perfil.html';
        } else {
            alert('Usuário ou senha inválidos!');
        }
    } catch (error) {
        console.error('Erro:', error);
        alert('Erro ao realizar o login.');
    }
}