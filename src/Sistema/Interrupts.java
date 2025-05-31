package src.Sistema;

public enum Interrupts { // possiveis interrupcoes que esta CPU gera
    noInterrupt, intEnderecoInvalido, intInstrucaoInvalida, intOverflow, intSTOP, timeOut, intIO, intPageFault;
}
