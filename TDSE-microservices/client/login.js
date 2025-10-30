// Configuración de Cognito User Pool
const REGION = 'us-east-1';
const USER_POOL_ID = 'us-east-1_iqWLwfBuk';
const APP_CLIENT_ID = '6bv9e50dsog1bd3oall4aentnr';
const API_URL = 'http://localhost:8084';

document.addEventListener('DOMContentLoaded', () => {

    const signInForm = document.getElementById('signInForm');
    const signUpForm = document.getElementById('signUpForm');
    const confirmArea = document.getElementById('confirmArea');
    const confirmBtn = document.getElementById('confirmBtn');
    const confirmCodeInput = document.getElementById('confirmCode');

    let userPool = null;

    function initCognito() {
        try {
            // Verifica que el SDK de Cognito se haya cargado
            if (typeof AmazonCognitoIdentity === 'undefined') {
                console.error('Amazon Cognito Identity SDK no encontrado. Asegúrate de cargar el CDN.');
                return false;
            }

            const poolData = {
                UserPoolId: USER_POOL_ID,
                ClientId: APP_CLIENT_ID
            };

            userPool = new AmazonCognitoIdentity.CognitoUserPool(poolData);
            console.log('Cognito User Pool inicializado correctamente');
            return true;
        } catch (e) {
            console.error('Error inicializando Cognito', e);
            return false;
        }
    }

    const cognitoReady = initCognito();


    // --- MANEJADORES DE EVENTOS ---

    if (!cognitoReady) {
        alert('Error crítico: Cognito no pudo inicializarse. Revisa la consola.');
        return;
    }

    // Iniciar Sesión
    if (signInForm) {
        signInForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const email = document.getElementById('signinEmail').value;
            const password = document.getElementById('signinPassword').value;

            try {
                console.log('Iniciando sesión con:', email);

                const authenticationData = {
                    Username: email,
                    Password: password,
                };

                const authenticationDetails = new AmazonCognitoIdentity.AuthenticationDetails(authenticationData);

                const userData = {
                    Username: email,
                    Pool: userPool
                };

                const cognitoUser = new AmazonCognitoIdentity.CognitoUser(userData);

                cognitoUser.authenticateUser(authenticationDetails, {
                    onSuccess: async (result) => {
                        console.log('Inicio de sesión exitoso:', result);
                        const accessToken = result.getAccessToken().getJwtToken();
                        const idToken = result.getIdToken().getJwtToken();

                        // Decodificar el token para obtener el cognitoId (sub)
                        const payload = JSON.parse(atob(idToken.split('.')[1]));
                        const cognitoId = payload.sub;

                        console.log('Cognito ID (sub):', cognitoId);
                        console.log('Access Token:', accessToken);

                        // Guarda los tokens en localStorage
                        localStorage.setItem('accessToken', accessToken);
                        localStorage.setItem('idToken', idToken);
                        localStorage.setItem('cognitoId', cognitoId);

                        // Consultar datos del usuario desde la API
                        try {
                            const userResponse = await fetch(`${API_URL}/users/${cognitoId}`, {
                                headers: {
                                    'Authorization': `Bearer ${accessToken}`,
                                    'Content-Type': 'application/json'
                                }
                            });

                            if (userResponse.ok) {
                                const userData = await userResponse.json();
                                console.log('Datos del usuario desde API:', userData);
                                localStorage.setItem('userData', JSON.stringify(userData));

                                await new Promise(resolve => setTimeout(resolve, 500));

                                alert('✅ Inicio de sesión exitoso. Bienvenido!');
                                window.location.href = 'index.html';
                            } else {
                                console.error('Usuario no encontrado en la API');
                                alert('⚠️ Inicio de sesión exitoso, pero no se encontraron datos del usuario en la API.');

                                await new Promise(resolve => setTimeout(resolve, 500));

                                window.location.href = 'index.html';
                            }
                        } catch (apiError) {
                            console.error('Error al consultar API:', apiError);
                            alert('⚠️ Inicio de sesión exitoso, pero hubo un problema al obtener tus datos.');

                            await new Promise(resolve => setTimeout(resolve, 500));

                            window.location.href = 'index.html';
                        }
                    },
                    onFailure: (err) => {
                        console.error('Error en signIn:', err);

                        let errorMessage = '';
                        if (err.code === 'NotAuthorizedException') {
                            errorMessage = '❌ Credenciales incorrectas. Verifica tu correo y contraseña.';
                        } else if (err.code === 'UserNotConfirmedException') {
                            errorMessage = '⚠️ Tu cuenta no está confirmada. Revisa tu correo e ingresa el código de confirmación.';
                            confirmArea.style.display = 'block';
                        } else if (err.code === 'UserNotFoundException') {
                            errorMessage = '❌ Usuario no encontrado. ¿Quieres registrarte?';
                        } else if (err.code === 'InvalidParameterException') {
                            errorMessage = '❌ Parámetros inválidos. Verifica los datos ingresados.';
                        } else if (err.code === 'TooManyRequestsException' || err.code === 'LimitExceededException') {
                            errorMessage = '⚠️ Demasiados intentos fallidos. Espera unos minutos e intenta de nuevo.';
                        } else if (err.message) {
                            errorMessage = `❌ Error: ${err.message}`;
                        } else {
                            errorMessage = '❌ Error desconocido al iniciar sesión. Intenta de nuevo.';
                        }

                        alert(errorMessage);
                    }
                });

            } catch (err) {
                console.error('Error en signIn:', err);
                alert('❌ Error inesperado al intentar iniciar sesión. Verifica tu conexión.');
            }
        });
    }

    // Registrarse
    if (signUpForm) {
        signUpForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const email = document.getElementById('signupEmail').value;
            const password = document.getElementById('signupPassword').value;

            try {
                const attributeList = [
                    new AmazonCognitoIdentity.CognitoUserAttribute({
                        Name: 'email',
                        Value: email
                    })
                ];

                userPool.signUp(email, password, attributeList, null, async (err, result) => {
                    if (err) {
                        console.error('Error en signUp:', err);

                        let errorMessage = '';
                        if (err.code === 'UsernameExistsException') {
                            errorMessage = '⚠️ Este correo ya está registrado. ¿Quieres iniciar sesión?';
                        } else if (err.code === 'InvalidPasswordException') {
                            errorMessage = '❌ Contraseña inválida. Debe tener al menos 8 caracteres, incluir mayúsculas, minúsculas, números y caracteres especiales.';
                        } else if (err.code === 'InvalidParameterException') {
                            errorMessage = '❌ Parámetros inválidos. Verifica que el correo sea válido y la contraseña cumpla los requisitos.';
                        } else if (err.message) {
                            errorMessage = `❌ Error: ${err.message}`;
                        } else {
                            errorMessage = '❌ Error al crear la cuenta. Intenta de nuevo.';
                        }

                        alert(errorMessage);
                        return;
                    }

                    console.log('Usuario registrado en Cognito:', result.user);
                    console.log('Cognito User Sub (ID):', result.userSub);

                    const cognitoId = result.userSub;

                    // Crear usuario en la API inmediatamente
                    try {
                        const apiResponse = await fetch(`${API_URL}/users`, {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json'
                            },
                            body: JSON.stringify({
                                cognitoId: cognitoId,
                                username: email.split('@')[0], // Usar parte del email como username
                                email: email
                            })
                        });

                        if (apiResponse.ok) {
                            const createdUser = await apiResponse.json();
                            console.log('✅ Usuario creado en la API:', createdUser);
                            alert('✅ Registro exitoso! Usuario creado en la API. Revisa tu correo para el código de confirmación.');
                        } else {
                            const errorText = await apiResponse.text();
                            console.error('❌ Error al crear usuario en la API:', errorText);
                            alert('✅ Registro exitoso en Cognito, pero hubo un problema al crear el usuario en la API. Revisa tu correo para confirmar la cuenta.');
                        }
                    } catch (apiError) {
                        console.error('❌ Error al llamar a la API:', apiError);
                        alert('✅ Registro exitoso en Cognito, pero no se pudo conectar con la API. Revisa tu correo para confirmar la cuenta.');
                    }

                    confirmArea.style.display = 'block';
                });
            } catch (err) {
                console.error('Error en signUp:', err);
                alert('❌ Error inesperado al crear la cuenta. Verifica tu conexión.');
            }
        });
    }

    // Confirmar Cuenta
    if (confirmBtn) {
        confirmBtn.addEventListener('click', async (e) => {
            const email = document.getElementById('signupEmail').value;
            const code = confirmCodeInput.value;

            if (!email || !code) {
                alert('⚠️ Por favor, ingresa el email usado para registrarte y el código de confirmación.');
                return;
            }

            try {
                const userData = {
                    Username: email,
                    Pool: userPool
                };

                const cognitoUser = new AmazonCognitoIdentity.CognitoUser(userData);

                cognitoUser.confirmRegistration(code, true, (err, result) => {
                    if (err) {
                        console.error('Error en confirmSignUp:', err);

                        let errorMessage = '';
                        if (err.code === 'CodeMismatchException') {
                            errorMessage = '❌ Código incorrecto. Verifica el código enviado a tu correo.';
                        } else if (err.code === 'ExpiredCodeException') {
                            errorMessage = '⚠️ El código ha expirado. Solicita un nuevo código.';
                        } else if (err.code === 'NotAuthorizedException') {
                            errorMessage = '⚠️ Usuario ya confirmado. Puedes iniciar sesión directamente.';
                        } else if (err.code === 'TooManyFailedAttemptsException') {
                            errorMessage = '❌ Demasiados intentos fallidos. Espera unos minutos.';
                        } else if (err.message) {
                            errorMessage = `❌ Error: ${err.message}`;
                        } else {
                            errorMessage = '❌ Error al confirmar la cuenta. Intenta de nuevo.';
                        }

                        alert(errorMessage);
                        return;
                    }

                    console.log('✅ Confirmación exitosa:', result);
                    alert('✅ Cuenta confirmada exitosamente! Ahora puedes iniciar sesión.');

                    // Limpiar formulario
                    confirmArea.style.display = 'none';
                    document.getElementById('signupEmail').value = '';
                    document.getElementById('signupPassword').value = '';
                    confirmCodeInput.value = '';
                });
            } catch (err) {
                console.error('Error en confirmSignUp:', err);
                alert('❌ Error inesperado al confirmar la cuenta. Verifica tu conexión.');
            }
        });
    }

});