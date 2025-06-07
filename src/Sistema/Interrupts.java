package src.Sistema;

public enum Interrupts { // possiveis interrupcoes que esta CPU gera
    intEnderecoInvalido, intInstrucaoInvalida, intOverflow, intSTOP, intPageFault, intIO, timeOut, noInterrupt;
}
