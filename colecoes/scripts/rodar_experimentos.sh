#!/usr/bin/env bash
# Roda os testes empíricos da seção 3 do relatório e grava os tempos em CSV.
#
# Uso (a partir da pasta colecoes, com o projeto já compilado em bin/):
#   bash scripts/rodar_experimentos.sh [repeticoes] [arquivo_csv]
#
# Para cada tamanho gera três arquivos em dados_teste/:
#   ordenado_N.txt    telefones crescentes   -> árvore degenerada
#   balanceado_N.txt  telefones "pelo meio"  -> árvore perfeitamente balanceada
#   aleatorio_N.txt   arquivo do trabalho de listas (util.GeradorEntrada)
# e executa contato.GerenciadorContatos de forma não-interativa: carrega o
# arquivo, pesquisa o pior caso por telefone, pesquisa o mesmo contato por
# nome e o remove por telefone.
set -euo pipefail

REPETICOES="${1:-3}"
CSV="${2:-dados_teste/resultados.csv}"
TAMANHOS="${TAMANHOS:-10000 25000 50000 100000}"
ESTRUTURAS="${ESTRUTURAS:-arvore lista}"

mkdir -p dados_teste
echo "estrutura,arquivo,n,repeticao,carga_ms,busca_telefone_ns,busca_nome_ns,remocao_ns,quantidade_final" > "$CSV"

# executar <rotulo> <opcao do menu inicial> <arquivo> <n> <nome> <telefone>
executar() {
    local rotulo="$1" opcao="$2" arquivo="$3" n="$4" nome="$5" telefone="$6"
    for rep in $(seq 1 "$REPETICOES"); do
        local saida
        saida=$(printf '%s\n1\n4\n%s\n3\n%s\n5\n%s\n7\n' "$opcao" "$telefone" "$nome" "$telefone" \
            | java -cp bin contato.GerenciadorContatos "$arquivo")
        local carga busca_tel busca_nome remocao final
        carga=$(echo "$saida" | grep -a "Tempo total de leitura" | sed -E 's/.*: ([0-9.,]+) ms.*/\1/' | tr ',' '.')
        busca_tel=$(echo "$saida" | grep -a "Tempo de busca" | sed -n 1p | sed -E 's/.*\(([0-9]+) ns\).*/\1/')
        busca_nome=$(echo "$saida" | grep -a "Tempo de busca" | sed -n 2p | sed -E 's/.*\(([0-9]+) ns\).*/\1/')
        remocao=$(echo "$saida" | grep -a "Tempo de remo" | sed -E 's/.*\(([0-9]+) ns\).*/\1/')
        final=$(echo "$saida" | grep -a "Quantidade total" | sed -E 's/.*: ([0-9]+).*/\1/')
        echo "$rotulo,$(basename "$arquivo"),$n,$rep,$carga,$busca_tel,$busca_nome,$remocao,$final" | tee -a "$CSV"
    done
}

valor() { echo "$1" | grep "^$2=" | cut -d= -f2-; }

for n in $TAMANHOS; do
    if [[ " $ESTRUTURAS " == *" arvore "* ]]; then
        info=$(java -cp bin util.GeradorArquivosOrdenados "$n" "dados_teste/ordenado_$n.txt")
        executar arvore_degenerada 3 "dados_teste/ordenado_$n.txt" "$n" \
            "$(valor "$info" PIOR_CASO_NOME)" "$(valor "$info" PIOR_CASO_TELEFONE)"

        info=$(java -cp bin util.GeradorArquivosBalanceados "$n" "dados_teste/balanceado_$n.txt")
        executar arvore_balanceada 3 "dados_teste/balanceado_$n.txt" "$n" \
            "$(valor "$info" PIOR_CASO_NOME)" "$(valor "$info" PIOR_CASO_TELEFONE)"
    fi
    if [[ " $ESTRUTURAS " == *" lista "* ]]; then
        info=$(java -cp bin util.GeradorEntrada "$n" "dados_teste/aleatorio_$n.txt")
        executar lista_nao_ordenada 2 "dados_teste/aleatorio_$n.txt" "$n" \
            "$(valor "$info" ULTIMO_NOME)" "$(valor "$info" ULTIMO_TELEFONE)"
        executar lista_ordenada 1 "dados_teste/aleatorio_$n.txt" "$n" \
            "$(valor "$info" ULTIMO_NOME)" "$(valor "$info" ULTIMO_TELEFONE)"
    fi
done
echo "Resultados gravados em $CSV"
