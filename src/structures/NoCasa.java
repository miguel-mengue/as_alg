package structures;

import model.Casa;

public class NoCasa {

    private Casa casa;

    private NoCasa proximo;
    private NoCasa anterior;

    public NoCasa(Casa casa) {
        this.casa = casa;
    }

    public Casa getCasa() {
        return casa;
    }

    public void setCasa(Casa casa) {
        this.casa = casa;
    }

    public NoCasa getProximo() {
        return proximo;
    }

    public void setProximo(NoCasa proximo) {
        this.proximo = proximo;
    }

    public NoCasa getAnterior() {
        return anterior;
    }

    public void setAnterior(NoCasa anterior) {
        this.anterior = anterior;
    }
}