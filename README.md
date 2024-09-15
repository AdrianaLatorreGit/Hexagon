<div align="center">
 <a href="https://youtu.be/bcM2klAkPfk" target="_blank">WATCH DEMO</a>
 </div>
 <br/>

# Documentação do Projeto

## 1. Configuração Inicial

Iniciei o projeto configurando o **View Binding**, que facilita o acesso às views do layout diretamente, eliminando a necessidade de `findViewById`. Com o View Binding, o código fica mais limpo e menos propenso a erros.

## 2. Dependências Adicionadas

Para garantir o funcionamento adequado da aplicação, adicionei as seguintes dependências:

- **Room Runtime**: Utilizado para a persistência de dados locais, permitindo uma interação mais intuitiva com o banco de dados SQLite.
- **Room Compiler**: Necessário para gerar o código que o Room usa para criar o banco de dados.
- **Room KTX**: Fornece extensões Kotlin que facilitam o uso do Room, especialmente ao trabalhar com corrotinas.
- **Picasso**: Biblioteca para carregar e armazenar imagens em cache, facilitando a exibição de fotos na aplicação.

## 3. Criação das Interfaces

Desenvolvi as seguintes interfaces para a aplicação:

- **Activity para Adicionar Usuários**: Tela para inserir dados dos usuários, como nome, data de nascimento, CPF e cidade. Inclui também a opção de adicionar uma imagem de perfil.
- **Activity para Visualizar Usuários Inativos**: Tela que lista usuários inativos com a opção de reativá-los, ideal para o gerenciamento de usuários que não estão mais ativos.
- **Dialog para Edição de Usuários**: Diálogo para editar rapidamente as informações dos usuários.
- **Tag para Visualização de Informações**: Tag reutilizável para mostrar detalhes dos usuários tanto na tela principal quanto na de usuários inativos.

## 4. Configuração do Banco de Dados com Room

- **DAO (Data Access Object)**: Criei um DAO para definir métodos de acesso ao banco de dados, como inserir, consultar e atualizar registros, organizando a interação com o banco e tornando o código mais limpo.
- **Banco de Dados**: Configurei o banco de dados usando o Room e implementei um padrão Singleton para garantir uma única instância do banco de dados em toda a aplicação. Isso evita problemas com múltiplas instâncias e garante eficiência.
- **Entity**: Defini uma Entity que representa a tabela no banco de dados. Cada instância desta Entity corresponde a uma linha na tabela, facilitando o armazenamento e a recuperação dos dados dos usuários.

- ![appInspector](https://github.com/user-attachments/assets/1cf09852-a8ce-4ff6-b181-47905db07bb2)


## 5. Implementação da `InactiveUsersActivity`

Na `InactiveUsersActivity`, fiz o seguinte:

- **Configuração**: Inicializei o banco de dados e carreguei a lista de usuários inativos.
- **Exibição**: Listei os usuários inativos com detalhes como nome, data de nascimento, CPF e cidade. Adicionei um switch para permitir a reativação dos usuários.
- **Reativação**: Quando o switch é ativado, o usuário é reativado no banco de dados e a tela principal é atualizada.

## 6. Implementação da `AddUserActivity`

Na `AddUserActivity`, implementei:

- **Configuração**: Inicializei o banco de dados e a interface do usuário, incluindo a opção para selecionar uma imagem de perfil.
- **Seleção de Imagem**: Adicionei a funcionalidade para escolher uma imagem da galeria e exibi-la na tela.
- **Validação e Salvamento**: Coletou e validou os dados inseridos. Se tudo estiver correto, os dados são salvos no banco de dados e uma mensagem de sucesso é exibida. Se houver um erro, uma mensagem de erro é mostrada.
- **Encerramento**: Após salvar o usuário com sucesso, a tela é fechada e retorna à tela anterior.

## TODO

- **Refatoração com RecyclerView**: Planejo ajustar o código para usar o `RecyclerView` em vez de adicionar views dinamicamente. O `RecyclerView` é mais eficiente para exibir listas longas, melhorando a performance e permitindo uma experiência de usuário mais fluida. Além disso, ele suporta diferentes tipos de visualizações e facilita a implementação de animações e interações mais complexas.

