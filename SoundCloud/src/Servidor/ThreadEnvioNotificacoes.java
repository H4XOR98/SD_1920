package Servidor;

import java.io.PrintWriter;
import java.util.List;

public class ThreadEnvioNotificacoes implements Runnable {
    private Sistema sistema;
    private GestorNotificacoes gestorNotificacoes;

    public ThreadEnvioNotificacoes(Sistema sistema, GestorNotificacoes gestorNotificacoes){
        this.sistema = sistema;
        this.gestorNotificacoes = gestorNotificacoes;
    }

    @Override
    public void run() {
        PrintWriter out;
        while(true){
            List<UtilizadorOnline> utilizadoresOnline = this.sistema.getUtilizadoresOnline();
            for(UtilizadorOnline utilizadorOnline : utilizadoresOnline){
                List<String> notificacoes = this.gestorNotificacoes.getNotificacoesUtilizador(utilizadorOnline.getNome());
                if(notificacoes != null){
                    for(String notificacao : notificacoes){
                        for(UtilizadorOnline u : utilizadoresOnline){
                            if(!utilizadorOnline.getNome().equals(u.getNome())){
                                out = u.getPrintWriter();
                                out.println(notificacao);
                                if(out.checkError() == false) {
                                    out.flush();
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

