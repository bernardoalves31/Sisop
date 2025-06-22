package src.Sistema;

public enum Interrupts { // possiveis interrupcoes que esta CPU gera
    intEnderecoInvalido, intInstrucaoInvalida, intOverflow, intSTOP, intPageFault, intLoaded, intIO, timeOut, noInterrupt;
}
