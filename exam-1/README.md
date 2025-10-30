# Parcial 1 TDSE

En este parcial se nos pidio realizar un patron fachada para accedes a nuesta API. Para ello, se hizo la siguiente organizacion:



En este podemos destacar los siguientes puntos o diseños de arquitectura:
- El servidor fachada se monta y brinda una interfaz al cliente, en la cual se interactua con el programa.
- El Facade se conecta con el backend mediante una HttpConection, haciendo las peticiones mediante este.
- El backend hace uso de la URL para pode retornar los elementos 
- Se tiene que ejecutar por aparte los dos servidores

<img width="1365" height="764" alt="imagen" src="https://github.com/user-attachments/assets/07691648-df8c-4bbb-8635-b7cd4f9f019a" />


# Instalacion

1. Clona el repositorio

```sh
git clone
```

2. Compila la app

```sh
mvn clean verify
```

3. Ejecuta los dos servidores
```sh
#Server
java -cp target\tdse-1.jar edu.escuelaing.tdse.backend.HttpServer
#Facade
java -cp target\tdse-1.jar edu.escuelaing.tdse.facade.FacadeServer
```
