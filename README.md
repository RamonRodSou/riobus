# RioBus API - Monitoramento de Frotas em Tempo Real

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Quarkus](https://img.shields.io/badge/Quarkus-4695EB?style=for-the-badge&logo=quarkus&logoColor=white)
![Clean Architecture](https://img.shields.io/badge/Architecture-Clean-success?style=for-the-badge)

## Objetivo do Projeto

O **RioBus** é o motor de back-end responsável por fornecer dados de geolocalização e tempo estimado de chegada (ETA) para ônibus em circulação na cidade do Rio de Janeiro.

Desenvolvido para alimentar um aplicativo mobile (React Native Expo), o sistema cruza dados abertos de mobilidade urbana com a localização do usuário, entregando informações precisas de rotas e frotas de forma limpa, rápida e sem poluição visual.

## Por que este projeto existe?

A solução foi idealizada para resolver um problema crônico na experiência de mobilidade urbana: a poluição de anúncios e a lentidão nos aplicativos de transporte tradicionais. O foco primário é a **Experiência do Usuário (UX)**, garantindo acesso instantâneo às informações de transporte sem interrupções publicitárias.

Pensado desde o início como uma plataforma escalável, o projeto visa uso pessoal inicial com uma estrutura de engenharia preparada para futura comercialização e monetização alternativa (ex: modelos Freemium para recursos avançados, licenciamento B2B ou parcerias de geolocalização não invasivas).

## Funcionalidades Principais

* **Busca Direcionada por Linha:** Rastreamento em tempo real de linhas específicas.
* **Monitoramento Multi-Linhas:** Capacidade de processar e enviar dados para o front-end renderizar múltiplas frotas simultaneamente, categorizadas por cores distintas no mapa.
* **Cálculo de ETA (Estimated Time of Arrival):** Processamento da distância entre os veículos em circulação e a geolocalização atual do usuário.
* **Roteamento Inteligente:** Motor de rotas que sugere o melhor trajeto combinando mapeamento de ruas com a posição atualizada da frota em tempo real.

## Arquitetura

O sistema foi desenhado utilizando princípios de **Clean Architecture**, garantindo total desacoplamento entre as regras de negócio, os serviços externos e as portas de entrada da aplicação. Isso permite que a API seja resiliente e de fácil manutenção.

```text
br.com.technosou
├── api          (Porta de Entrada: Controllers/Resources consumidos pelo React Native)
│    ├── controller
│    └── dto     (Modelos de dados limpos, sanitizados e otimizados para o Mobile)
│
├── core         (Coração do sistema: Regras de negócio puras e agnósticas)
│    ├── model   (Entidades de domínio do transporte público)
│    ├── service (Lógica complexa: cálculo de tempo, filtragem de linhas, cruzamento de matriz de distância)
     └── util    (Métodos re-utilizaveis)
│
└── infra        (Porta de Saída: Integrações com o mundo externo)
     ├── client  (REST Clients para consumo de APIs de terceiros, como o Data.Rio)
     ├── dto     (Mapeamento bruto dos dados provenientes da prefeitura)
     └── entity  (Entidade para comunicação com banco de dados)