# :pager: sistema_computacional_hipotetico-Z808
Sistema Computacional Hiipotetico Z808 baseado na arquitetura dos processadores da família INTEL 8086/88 desenvolvido como trabalho avaliativo da disciplina de Programação de Sistemas

### Grupo Adas & CG
:woman_technologist: [@BiancaBDullius](https://github.com/BiancaBDullius) <br />
:woman_technologist: [@Caroline-Camargo](https://github.com/Caroline-Camargo) <br />
:man_technologist: [@CloudioJ](https://github.com/CloudioJ) <br />
:woman_technologist: [@dudaac1](https://github.com/dudaac1) <br />
:man_technologist: [@SteinGB](https://github.com/SteinGB) <br />
:woman_technologist: [@jjuliar](https://github.com/jjuliar) <br />
:woman_technologist: [@juliaveiga](https://github.com/juliaveiga) <br />
:woman_technologist: [@majudlorenzoni](https://github.com/majudlorenzoni) <br />
:woman_technologist: [@Yasmin-Camargo](https://github.com/Yasmin-Camargo) <br />

## :play_or_pause_button: Como Executar
Este projeto foi desenvolvido com o auxílio da IDE _Apache Netbeans 17.0_ com o _JDK 17_.<br>
Para executar o programa, clone o repositório: <br>  `git@github.com:Yasmin-Camargo/sistema_computacional_hipotetico-Z808.git`<br> A execução do projeto deve ser feita a partir da classe JanelaInicia.java.

:pushpin: Caso encontre dificuldades para executar, experimente baixar por este [link](https://www.oracle.com/java/technologies/downloads/) 

## :clipboard: Etapas de desenvolvimento
O processo de desenvolvimento do simulador Z808 consistiu em quatro fases distintas: Executor, Montador, Processador de Macros, Carregador e Ligador

![image](https://github.com/Yasmin-Camargo/sistema_computacional_hipotetico-Z808/assets/88253809/458d5e42-cd38-4ff1-a8ee-f1d936ef1878)

### :one: Executor
Focado na execução das instruções própriamente ditas e em uma interface visual.

![image](https://github.com/Yasmin-Camargo/sistema_computacional_hipotetico-Z808/assets/88253809/72163134-3db4-4a9d-a112-e5d3b17dd872)

### :two: Montador
Foi implementado o montador de dois passos, sendo responsável pela tradução do código-fonte em código de máquina.

![image](https://github.com/Yasmin-Camargo/sistema_computacional_hipotetico-Z808/assets/88253809/9fd93679-4437-4df6-a7a3-29a30dce575c)

### :three: Processador de Macros
Etapa dedicada a expansão de macros, foi desenvolvido em uma só passagem permitindo macros aninhadas e chamadas aninhadas.

### :four: Carregador e Ligador
Nesta fase é realizado o carregamento e a vinculação de módulos para formar um programa funcional, sendo feito em duas passagens.


## :mag_right: Códigos das Instruções

| Operação  |  Código de Máquina| Modo de endereçamento |
| --------  | ------------------| --------------------- |
add AX, AX  | 03 C0             | Registrador           
add AX, DX  | 03 C2             | Registrador
add AX, opd | 05                | Direto
add AX, opd | 04                | Imediato
div AX, SI  | F7 F6             | Registrador
div AX, AX  | F7 C0             | Registrador
sub AX, AX  | 2B C0             | Registrador
sub AX, DX  | 2B C2             | Registrador
sub AX, opd | 2D                | Direto
sub AX, opd | 2C                | Imediato
mul AX, SI  | F7 F5             | Registrador
mul AX, AX  | F7 F0             | Registrador
cmp AX,opd  | 3D                | Direto
cmp AX,opd  | 3C                | Imediato
cmp AX,DX 3B| C2                | Registrador
and AX,AX   | 23 C0             | Registrador
and AX,DX   | 23 C2             | Registrador
and AX,opd  | 25                | Direto
and AX,opd  | 24                | Imediato 
not AX      | F8 C0             | Registrador
or AX, AX   | 0B C0             | Registrador
or AX, DX   | 0B C2             | Registrador
or AX,opd   | 0D                | Direto
or AX,opd   | 0C                | Imediato
xor AX,AX   | 33 C0             | Registrador
xor AX,DX   | 33 C2             | Registrador
xor AX,opd  | 35                | Direto
xor AX,opd  | 34                | Imediato
jmp opd     | EB                | Direto
jz opd      | 74                | Direto
jnz opd     | 75                | Direto
jp opd      | 7A                | Direto
call opd    | E8                | Imediato
call opd    | E7                | Direto
ret         | EF                | -
hlt         | EE                | -
pop reg     | 57 C0             | Registrador
pop reg     | 57 C2             | Registrador
pop opd     | 59                | Direto
pop opd     | 58                | Imediato
popf        | 9D                | Registrador
push reg    | 50 C0             | Registrador
push reg    | 50 C2             | Registrador
pushf       | 9C                | Registrador
store reg   | 07 C0             | Registrador
store reg   | 07 C2             | Registrador
read opd    | 12                | Imediato
read opd    | 13                | Direto
write opd   | 08                | Imediato
write opd   | 09                | Direto
mov AX, opd | 14                | Imediato
mov AX, opd | 15                | Direto
mov AX, DX  | 16 C2             | Registrador
