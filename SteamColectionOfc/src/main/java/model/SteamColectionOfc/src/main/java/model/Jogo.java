package model;


public class Jogo {

    private int id;
    private String nome;
    private String genero;
    private String plataforma;
    private String dataLancamento;
    private String status;
    private String caminhoFoto;

    
    
 public Jogo() {
    }
    
    public Jogo(String nome, String genero, String plataforma, String dataLancamento, String status, String caminhoFoto) {
        
        this.nome = nome;
        this.genero = genero;
        this.plataforma = plataforma;
        this.dataLancamento = dataLancamento;
        this.status = status;
        this.caminhoFoto = caminhoFoto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getPlataforma() {
        return plataforma;
    }

    public void setPlataforma(String plataforma) {
        this.plataforma = plataforma;
    }

    public String getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(String dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }

    @Override
    public String toString() {
        return "Jogo{" + "id=" + id + ", nome=" + nome + ", genero=" + genero + ", plataforma=" + plataforma + ", dataLancamento=" + dataLancamento + ", status=" + status + ", caminhoFoto=" + caminhoFoto + '}';
    }
}