package Cliente;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class ThreadLeituraNotificacao implements Runnable{
    private SistemaRemoto sistemaRemoto;
    private AtomicBoolean execucao;

    public ThreadLeituraNotificacao(SistemaRemoto sistemaRemoto, AtomicBoolean execucao){
        this.sistemaRemoto = sistemaRemoto;
        this.execucao = execucao;
    }

    @Override
    public void run() {
        try {
            while(this.execucao.get() == true) {
                sistemaRemoto.leNotificacao();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}