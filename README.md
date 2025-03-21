# SistemaCompras
# Descrição
O Sistema de Compras é uma aplicação Java desenvolvida para gerenciar compras de itens, permitindo o cadastro de empresas, fornecedores e itens, além de funcionalidades para filtrar, exportar e gerar relatórios. A aplicação foi projetada para ser uma ferramenta prática para empresas que precisam organizar suas compras, com suporte a banco de dados SQLite, exportação para Excel e geração de pedidos em PDF.

Este projeto foi desenvolvido como parte de um exercício de programação, utilizando Java com a biblioteca Swing para a interface gráfica, Maven para gerenciamento de dependências e várias bibliotecas externas para funcionalidades específicas, como manipulação de Excel e PDF.

# Funcionalidades
Gerenciamento de Itens:
Adicionar, remover e atualizar itens.
Marcar itens como "Comprado" ou "Parcialmente Comprado".
Filtrar itens por status (A Comprar, Comprado, Parcialmente Comprado) e fornecedor.
Gerenciamento de Empresas e Fornecedores:
Cadastrar, alterar e excluir empresas.
Cadastrar, alterar e excluir fornecedores.
Exportação e Importação:
Exportar lista de itens para Excel.
Importar preços de itens a partir de um arquivo Excel.
Geração de Pedidos:
Gerar pedidos em PDF com base nos itens selecionados, incluindo informações da empresa e do fornecedor.
Interface Gráfica:
Interface amigável construída com Java Swing.
Suporte a fundo personalizado com uma imagem de logo (Logo.jpg).

# Tecnologias Utilizadas
Java: Linguagem principal (versão 17).
Maven: Gerenciamento de dependências e build.
SQLite: Banco de dados leve para armazenamento de dados.
Java Swing: Biblioteca para a interface gráfica.

# Dependências:
sqlite-jdbc (3.46.1): Para conexão com o banco de dados SQLite.
gson (2.10.1): Para serialização/deserialização de JSON.
poi e poi-ooxml (5.2.5): Para manipulação de arquivos Excel.
itextpdf (5.5.13.3): Para geração de PDFs.

# Pré-requisitos
Antes de executar o projeto, certifique-se de que você tem os seguintes requisitos instalados:

# Java Development Kit (JDK): Versão 17 ou superior.
Maven: Para gerenciar dependências e compilar o projeto.
Eclipse IDE (opcional): Para desenvolvimento e execução do projeto.
Logo.jpg: Um arquivo de imagem para o fundo da interface (deve ser colocado na mesma pasta do JAR executável).
