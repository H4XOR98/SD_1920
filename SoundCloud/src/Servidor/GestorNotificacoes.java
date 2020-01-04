package Servidor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class GestorNotificacoes {
    private Map<String, List<Notificacao>> notificacoes;
    private ReentrantLock lockNotificacoes;

    public GestorNotificacoes(){
        this.notificacoes = new HashMap<>();
        this.lockNotificacoes = new ReentrantLock(true);
    }

    public void adicionaNotificacao(String nome, Notificacao notificacao) {
        this.lockNotificacoes.lock();
        if(!this.notificacoes.containsKey(nome)){
            this.notificacoes.put(nome,new ArrayList<>());
        }
        this.notificacoes.get(nome).add(notificacao);
        this.lockNotificacoes.unlock();
    }

    public List<String> getNotificacoesUtilizador(String nome){
        List<String> resultado = new ArrayList<>();
        this.lockNotificacoes.lock();
        for(Notificacao notificacao : this.notificacoes.get(nome)){
            resultado.add("notificacao;" + notificacao.toString());
        }
        this.lockNotificacoes.unlock();
        return resultado;
    }
}
