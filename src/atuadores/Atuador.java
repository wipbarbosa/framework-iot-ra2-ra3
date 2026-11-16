package atuadores;

import excecoes.AtuadorIndisponivelException;
import modelo.Dispositivo;

public abstract class Atuador extends Dispositivo {

    private static final long serialVersionUID = 1L;

    private boolean ligado;
    private boolean disponivel;

        protected Atuador(String id, String nome, String comodoNome, int x, int y){
            
            super(id, nome, comodoNome, x, y);
            this.ligado = false;
            this.disponivel = true;
        }

        public boolean isLigado(){
            return ligado;
        }

        public boolean isDisponivel(){
            return disponivel;
        }

        public void setDisponivel(boolean disponivel){
            this.disponivel = disponivel;
        }

    
        public void liga() throws AtuadorIndisponivelException{
            verificarDisponibilidade();
            ligado = true;
        }

        public void desliga() throws AtuadorIndisponivelException{
            verificarDisponibilidade();
            ligado = false;
        }

        protected void verificarDisponibilidade() throws AtuadorIndisponivelException {
            if (!disponivel) {
                throw new AtuadorIndisponivelException("O atuador " + getNome() + " esta indisponivel");
            }
        }
    }