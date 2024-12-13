package Entidade;

public class Funcionario {
    private int codFunc;
    private String nomeFunc;
    private double salario;
    private String cargo;

    public Funcionario(int codFunc, String nomeFunc, double salario, String cargo) {
        this.codFunc = codFunc;
        this.nomeFunc = nomeFunc;
        this.salario = salario;
        this.cargo = cargo;
    }

    public int getCodFunc() {
        return codFunc;
    }

    public String getNomeFunc() {
        return nomeFunc;
    }

    public double getSalario() {
        return salario;
    }

    public String getCargo() {
        return cargo;
    }
}
