package src.Sistema;

public enum Interrupts { // possiveis interrupcoes que esta CPU gera
    intEnderecoInvalido, intInstrucaoInvalida, intOverflow, intSTOP, intPageFault, intLoaded, intPageSaved, intVMLoad, intIO, timeOut, noInterrupt;
}
