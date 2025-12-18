# Documentação Técnica: Calculadora de Saúde e IMC

## 1. Visão Geral
Este documento descreve a implementação de um aplicativo Android nativo desenvolvido em **Kotlin** e **Jetpack Compose**. O objetivo principal foi evoluir uma calculadora de IMC simples para uma suíte de saúde completa, aplicando arquitetura MVVM, persistência de dados local e métricas fisiológicas adicionais.

## 2. Arquitetura e Decisões Técnicas
O projeto foi refatorado a partir do código base para aderir ao padrão **MVVM (Model-View-ViewModel)**.

* **Camada de Apresentação (UI):** Utiliza **Jetpack Compose**. As telas (`HomeScreen`, `HistoryScreen`) são funções *stateless* (sem estado complexo interno) que reagem aos dados observados do ViewModel.
* **ViewModel:** O `HealthViewModel` gerencia o estado da UI e as regras de negócio. Utiliza `StateFlow` para emitir atualizações de dados de forma reativa e segura para o ciclo de vida do Android. 
* **Camada de Dados:** Implementada com a biblioteca **Room**. A persistência é local, garantindo que o histórico do usuário seja mantido mesmo sem internet ou após reiniciar o aparelho.
* **Camada de Domínio:** A lógica matemática foi isolada no objeto `HealthCalculator`, garantindo que fórmulas não se misturem com código de interface.

## 3. Modelo de Dados e Persistência
A persistência utiliza uma tabela única (`health_records`) definida pela entidade `HealthRecord`. 

Cada registro armazena:
* `id`: Chave primária autogerada.
* `date`: Timestamp (`Long`) da medição.
* `weight`, `height`, `age`: Dados de entrada.
* `imc`, `tmb`, `bodyFat`: Métricas calculadas.

A ordenação padrão é decrescente por data (mais recente primeiro), facilitando a visualização da evolução do usuário.

## 4. Fórmulas Utilizadas
Além do IMC padrão ($peso / altura^2$), foram implementadas:

### Taxa Metabólica Basal (TMB)
Utilizou-se a equação de **Mifflin-St Jeor**, considerada atualmente a mais precisa para estimativa sem calorimetria direta.

* **Homens:** $(10 \times peso) + (6.25 \times altura) - (5 \times idade) + 5$
* **Mulheres:** $(10 \times peso) + (6.25 \times altura) - (5 \times idade) - 161$

### Estimativa de Gordura Corporal (Body Fat)
Fórmula baseada no IMC, idade e sexo (Deurenberg et al.), útil para triagem geral.

* **Fórmula:** $(1.20 \times IMC) + (0.23 \times idade) - (10.8 \times sexo) - 5.4$
    * *(Onde sexo=1 para homens, 0 para mulheres)*

### Necessidade Calórica Diária
Produto da TMB pelo **Fator de Atividade** selecionado pelo usuário:
* Ex: Sedentário = 1.2, Moderado = 1.55, etc.

## 5. Melhorias Futuras
Para versões futuras, planejamos:

* **Gráficos:** Implementação da biblioteca *Vico* ou *MPAndroidChart* para plotar a curva de evolução do peso.
* **Autenticação:** Integração com **Firebase Auth** para salvar dados na nuvem.
* **Perfis Múltiplos:** Permitir que diferentes usuários usem o mesmo dispositivo com históricos separados.
