<script>
    function submitLoginForm() {
        const form = this;

        form.username.value = form.username.value.trim();

        if (form.username.value.length === 0) {
            alert('Username is required');
            form.username.focus();
            return;
        }

        form.password.value = form.password.value.trim();

        if (form.password.value.length === 0) {
            alert('Password is required');
            form.password.focus();
            return;
        }

        fetch('http://localhost:8080/api/v1/members/login', {
            method: 'POST',
            credentials: 'include', // 핵심 변경점
            credentials: 'include', // <= 변경점(없으면 쿠키에 토큰 추가 안됨)
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                username: form.username.value,
                password: form.password.value
            })
        })
            .then(response => response.json())
            .then(data => {
                console.log(data);
            })
            .catch(error => {
                console.error(error);
            });
    }
</script>

<form on:submit|preventDefault={submitLoginForm}>
    <input type="text" name="username" placeholder="Username" />
    <input type="password" name="password" placeholder="Password" />
    <button type="submit">Login</button>
</form>