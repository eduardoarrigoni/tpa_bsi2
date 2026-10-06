## Como rodar os exemplos no VS Code

Para garantir que a IDE reconheça o projeto corretamente, siga estes passos:

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/felipesoeiroa/trabalho_tpa
    ```
2.  **Abra o VS Code.**
3.  Vá em `File > Open Folder...` e selecione a pasta **`colecoes`**.
    > **Importante:** Não abra a pasta raiz do repositório se quiser que o suporte ao Java (IntelliSense) funcione perfeitamente.
4.  Certifique-se de ter o **Extension Pack for Java** da Microsoft instalado.
5.  Abra `contato/GerenciadorContatos.java` (agenda de contatos) e clique em **Run** acima do método `main`.

---

## Comandos Úteis (Terminal)

Caso prefira rodar via terminal sem usar o botão "Run" da IDE:

Rode os comandos abaixo a partir da pasta `colecoes` (é onde fica o `entrada.txt` usado pelo programa de contatos).

**Para compilar:**
* No Linux/Mac:
  ```bash
  javac $(find src -name "*.java") -d bin
  ```
* No Windows (PowerShell):
  ```powershell
  javac (Get-ChildItem -Recurse src/*.java) -d bin
  ```

**Para executar a agenda de contatos:**
```
java -cp bin contato.GerenciadorContatos
```
O programa pergunta a estrutura (1 - lista ordenada, 2 - lista não-ordenada, 3 - árvore binária). Para carregar outro arquivo em vez de `entrada.txt`, passe o caminho como argumento:
```
java -cp bin contato.GerenciadorContatos dados_teste/balanceado_10000.txt
```

**Para gerar arquivos de teste (árvore degenerada e árvore balanceada):**
```
java -cp bin util.GeradorArquivosOrdenados 10000 dados_teste/ordenado_10000.txt
java -cp bin util.GeradorArquivosBalanceados 10000 dados_teste/balanceado_10000.txt
```

**Para repetir todas as medições do relatório** (Linux, Mac ou Git Bash no Windows):
```bash
bash scripts/rodar_experimentos.sh 3 dados_teste/resultados.csv
```
O primeiro argumento é o número de repetições por arquivo. As variáveis `TAMANHOS` e `ESTRUTURAS` restringem a execução, por exemplo `TAMANHOS="10000 25000" ESTRUTURAS=arvore bash scripts/rodar_experimentos.sh`. As execuções com árvore degenerada e com listas levam vários minutos nos tamanhos maiores.
