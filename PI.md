<div style="text-align: center;">
  <img src=".assets/logo/by_gw.png" alt="Logo_gb-oaz" style="width: 30%; height: auto;">
</div>

# **QuizMaster - Seu desafio de conhecimento!**

O **QuizMaster** é um aplicativo de quiz dinâmico e interativo, inspirado em plataformas como Kahoot e Quizizz. Ele permite criar, compartilhar e jogar quizzes personalizados em tempo real, tornando o aprendizado e a diversão acessíveis para todos.

## **O que é o QuizMaster?**
- Uma plataforma gamificada para desafios de conhecimento.
- Permite que professores, alunos e empresas criem quizzes interativos.
- Oferece partidas ao vivo e desafios assíncronos para diferentes públicos.

## **Para que serve?**
- **Para educadores:**
  - Criar quizzes para avaliar e engajar alunos.
  - Estimular o aprendizado de forma lúdica.
- **Para estudantes:**
  - Testar conhecimentos de maneira divertida.
  - Reforçar conteúdos de estudo com desafios personalizados.
- **Para empresas:**
  - Aplicar treinamentos interativos.
  - Realizar dinâmicas de equipe com quizzes personalizados.

## **Como funciona?**
1. O criador do quiz configura perguntas e opções de resposta.
2. Os participantes acessam a sala com um código.
3. O quiz é jogado em tempo real ou de forma assíncrona.
4. Os resultados são exibidos em um ranking dinâmico.

## **Benefícios**
- **Interatividade:** Partidas dinâmicas e envolventes.
- **Acessibilidade:** Disponível para diferentes dispositivos.
- **Personalização:** Crie quizzes sob medida para suas necessidades.
- **Engajamento:** Torne o aprendizado ou treinamentos mais estimulantes.

## **Equipe de Desenvolvimento**
Projeto desenvolvido no **PI - Projeto Integrador** da **Univesp**.

## **Autor(es)**
- [@gustavoboaz](https://github.com/GustavoBoaz)
- [@guimarangon](https://github.com/GustavoBoaz)
- [@guioliveira](https://github.com/GustavoBoaz)
- [@gugatto](https://github.com/GustavoBoaz)

## **Técnologias Utilizadas**
![Linux](https://img.shields.io/badge/Linux--orange?style=for-the-badge)
![Windows](https://img.shields.io/badge/Windows--brown?style=for-the-badge)

![Git](https://img.shields.io/badge/Git--purple?style=for-the-badge)
![Shell](https://img.shields.io/badge/Shell--magenta?style=for-the-badge)
![Docker](https://img.shields.io/badge/Docker--blue?style=for-the-badge)
![Kubernetes](https://img.shields.io/badge/Kubernetes--aqua?style=for-the-badge)

![Java](https://img.shields.io/badge/Java--red?style=for-the-badge)
![Maven](https://img.shields.io/badge/Maven--green?style=for-the-badge)
![Spring](https://img.shields.io/badge/Spring--indigo?style=for-the-badge)

![Javascript](https://img.shields.io/badge/Javascript--coral?style=for-the-badge)
![Typescript](https://img.shields.io/badge/Typescript--navy?style=for-the-badge)
![Cypress](https://img.shields.io/badge/Cypress--brown?style=for-the-badge)
![Vue](https://img.shields.io/badge/Vue--lime?style=for-the-badge)

![CleanArchitecture](https://img.shields.io/badge/Clean_Architecture--pink?style=for-the-badge)
![DesignPatters](https://img.shields.io/badge/Design_Patters--violet?style=for-the-badge)
![CleanCode](https://img.shields.io/badge/Clean_Code--yellow?style=for-the-badge)
![TDD](https://img.shields.io/badge/TDD--teal?style=for-the-badge)
![BDD](https://img.shields.io/badge/BDD--crimson?style=for-the-badge)

![UML](https://img.shields.io/badge/UML--white?style=for-the-badge)
![Markdown](https://img.shields.io/badge/Markdown--black?style=for-the-badge)
![Gherkin](https://img.shields.io/badge/Gherkin--green?style=for-the-badge)

## **Licença**
Este projeto é de uso acadêmico e não possui fins comerciais.

<br>

---

# Diagramas geral
<details>                                                                          
    <summary>🛠   Visualizar</summary>

</details>

# Detalhes técnicos
<details>
  <summary>🛠 Visualizar</summary>

## Linux

```bash
java --version
openjdk 21.0.3 2024-04-16 LTS
OpenJDK Runtime Environment Temurin-21.0.3+9 (build 21.0.3+9-LTS)
OpenJDK 64-Bit Server VM Temurin-21.0.3+9 (build 21.0.3+9-LTS, mixed mode, sharing)
```
```bash
mvn --version
Apache Maven 3.6.3
Maven home: /usr/share/maven
Java version: 21.0.3, vendor: Eclipse Adoptium, runtime: /home/gustavo/.sdkman/candidates/java/21.0.3-tem
Default locale: en_US, platform encoding: UTF-8
OS name: "linux", version: "6.5.0-15-generic", arch: "amd64", family: "unix"
```
```bash
docker --version
Docker version 25.0.1
```
```bash
docker-compose --version
docker-compose version 1.29.2
```
## Windows

```bash
java --version
java 21.0.1 2023-10-17 LTS
Java(TM) SE Runtime Environment (build 21.0.1+12-LTS-29)
Java HotSpot(TM) 64-Bit Server VM (build 21.0.1+12-LTS-29, mixed mode, sharing)
```
```bash
mvn --version
Apache Maven 3.9.6 (bc0240f3c744dd6b6ec2920b3cd08dcc295161ae)
Maven home: C:\Program Files\Maven\apache-maven-3.9.6
Java version: 21.0.1, vendor: Oracle Corporation, runtime: C:\Program Files\Java\jdk-21
Default locale: pt_BR, platform encoding: UTF-8
OS name: "windows 11", version: "10.0", arch: "amd64", family: "windows"
```
```bash
docker --version
Docker version 26.1.1, build 4cf5afa
```
```bash
docker-compose --version
Docker Compose version v2.27.0-desktop.2
```

</details>

# Montar e testar
<details>
  <summary>🛠 frontend</summary>

## Passos para montar a aplicação frontend

1. **Acesse a pasta do frontend:**
   Navegue até o diretório onde está o código do frontend:
   ```bash
   cd frontend
   ```

2. **Build da aplicação:**
   Para compilar todos os pacotes de frontend, execute o comando:
   ```bash
   yarn build
   ```

3. **Iniciar a aplicação:**
   Após o build ser concluído, inicie o servidor de desenvolvimento com o comando:
   ```bash
   yarn start
   ```

Isso irá compilar e executar todos os pacotes do frontend, deixando a aplicação pronta para rodar.

</details>

<br>

<details>
  <summary>🛠 backend</summary>

</details>

<br>

---

