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

    const url = tipoCadastro === 'empresa' ? '/empresa/login' : '/usuario/login';

    const data = {
        email: emailUsuario,
        senha: senhaLogin
    };

    try {
        const response = await fetch(`http://localhost:6789${url}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        const dados = await response.json(); 

        if (response.ok && dados.sucesso) {
            localStorage.setItem('usuarioId', dados.id);
            localStorage.setItem('tipoCadastro', tipoCadastro);

            alert('Login realizado com sucesso!');
            window.location.href = 'perfil.html';
        } else {
            alert(dados.mensagem || 'Erro ao realizar o login.');
        }
    } catch (error) {
        console.error('Erro:', error);
        alert('Erro ao realizar o login.');
    }
}

