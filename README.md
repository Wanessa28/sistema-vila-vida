# 🏡 Sistema Vila Vida

## Apresentação

O **Sistema Vila Vida** é uma aplicação web desenvolvida como projeto acadêmico de conclusão de curso (TCC) do curso de Tecnologia em Sistemas para Internet, do Instituto Federal de Mato Grosso (IFMT).

O sistema tem como finalidade auxiliar na organização e no gerenciamento das informações de uma instituição de acolhimento de pessoas idosas, proporcionando maior praticidade, segurança e eficiência nos processos administrativos.

## Objetivo

Desenvolver uma solução informatizada para centralizar as informações relacionadas aos moradores, às moradias e aos funcionários da instituição Vila Vida, facilitando o cadastro, a consulta, a atualização e a emissão de relatórios.

## Funcionalidades

- **Gerenciamento de moradores:** cadastro, consulta, edição e visualização de informações.
- **Gerenciamento de moradias:** acompanhamento e administração das unidades habitacionais.
- **Gerenciamento de funcionários:** cadastro, consulta, edição e visualização de dados.
- **Relatórios administrativos:** apresentação de informações sobre moradores, moradias e funcionários.
- **Painel principal:** acesso organizado às funcionalidades do sistema.
- **Autenticação:** controle de acesso por meio de uma tela de login.

## Tecnologias utilizadas

- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- Thymeleaf
- HTML e CSS
- MySQL
- Apache Maven
- Git e GitHub
- GitHub Codespaces

## Estrutura do projeto

O projeto utiliza uma organização em camadas:

- **Controller:** recebe e processa as requisições da aplicação.
- **Model:** representa as entidades do sistema.
- **Repository:** realiza a comunicação com o banco de dados.
- **Service:** concentra regras de negócio.
- **Templates:** contém as páginas HTML renderizadas pelo Thymeleaf.

## Configuração e execução

A aplicação utiliza o MySQL como banco de dados e variáveis de ambiente para proteger informações de autenticação.

As variáveis utilizadas incluem:

- `VILAVIDA_DB_PASSWORD`
- `VILAVIDA_LOGIN_USUARIO`
- `VILAVIDA_LOGIN_SENHA`

Essas informações devem ser configuradas no ambiente de execução e não devem ser publicadas no repositório.

No ambiente de desenvolvimento configurado no GitHub Codespaces, o sistema pode ser iniciado pelo comando:

```bash
bash iniciar.sh
```

A aplicação utiliza a porta `8080`.

## Segurança e privacidade

O sistema utiliza o Spring Security para autenticação e controle de acesso.

As credenciais de acesso e os arquivos de backup do banco de dados devem permanecer fora do repositório público, contribuindo para a proteção das informações da instituição.

## Contexto acadêmico

**Projeto:** Sistema Vila Vida  
**Curso:** Tecnologia em Sistemas para Internet  
**Instituição de ensino:** Instituto Federal de Mato Grosso (IFMT)  
**Modalidade:** Projeto de Trabalho de Conclusão de Curso (TCC)

## Situação do projeto

O sistema possui funcionalidades implementadas para gerenciamento de moradores, moradias e funcionários, além de autenticação, painel administrativo e relatórios.

O projeto encontra-se em fase de revisão, documentação e preparação para apresentação acadêmica.# sistema-vila-vida
Sistema web para gerenciamento de moradores e moradias da instituição Vila Vida
