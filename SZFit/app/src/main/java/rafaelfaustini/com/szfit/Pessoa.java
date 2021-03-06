package rafaelfaustini.com.szfit;

import android.content.Context;
import android.content.res.Resources;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

import static android.provider.Settings.System.getString;

public class Pessoa{
    String nome;
    int idade;
    int altura;
    double peso;
    double imc;
    char genero;
    String saude;
    int nivel;
    Context context;
    ArrayList<Double> ideal = new ArrayList<Double>();

    public Pessoa(Context context){
      this.context = context;
    }
    public Double getIdealMin() {
        if (this.ideal.size() >= 1) {
            return this.ideal.get(0);
        }
        return 0.0;
    }

    public Double getIdealMax() {
        if (this.ideal.size() >= 2) {
            return this.ideal.get(1);
        }
        return 0.0;
    }

    public double getPeso() {
        return this.peso;
    }

    public int getAlturaCM() {
        return this.altura;
    }

    public int getNivel(){
        return this.nivel;
    }

    public double getAlturaM() {
        return (this.getAlturaCM() / 100.0);
    }

    public int getIdade() {
        return this.idade;
    }

    public String getSaude(){
        return this.saude;
    }

    private static String arredondar(double media) {
        DecimalFormat df = new DecimalFormat("0.0");
        df.setRoundingMode(RoundingMode.HALF_UP);
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(dfs);
        return df.format(media);
    }

    public double getImc(){
        String teste = arredondar(this.imc);
        return Double.valueOf(teste);
    }
    public String getNome() {
        return this.nome;
    }

    private static boolean isBetween(double x, double lower, double upper) {
        return lower <= x && x <= upper;
    }

    private void setNivel(int nivel) {
        this.nivel = nivel;
    }

    private void setIdeal(double min, double max) {
        if(this.ideal.size() > 0){
            this.ideal.clear();
        }
        this.ideal.add(min);
        this.ideal.add(max);
    }

    public int faixaMenor(){
        Double min;
        double k = Math.pow(this.getAlturaM(), 2);
        k = Double.valueOf(String.format(Locale.US, "%.2f", k));
        min = 24.9 * k;
        min = Math.ceil(Double.parseDouble(String.format(Locale.US, "%.1f", min)));
        int value =  min.intValue();
        return value;
    }
    public int faixaMaior(){
    Double max;
        double k = Math.pow(this.getAlturaM(), 2);
        k = Double.valueOf(String.format(Locale.US, "%.2f", k));
        max = 18.6 * k;
        max = Math.ceil(Double.parseDouble(String.format(Locale.US, "%.1f", max)));
        int value =  max.intValue();
        return value;
    }

    private void calcularIdeal() {
        Double min;
        Double max;
        Double peso = this.getPeso();
        double k = Math.pow(this.getAlturaM(), 2);
        k = Double.valueOf(String.format(Locale.US, "%.2f", k));



        min = 24.9 * k;
        min = Math.ceil(Double.parseDouble(String.format(Locale.US, "%.1f", min)));
        min = peso - min;
        min = Double.parseDouble(String.format(Locale.US, "%.1f", min));

        max = 18.6 * k;
        max = Math.ceil(Double.parseDouble(String.format(Locale.US, "%.1f", max)));
        max = peso - max;
        max = Double.parseDouble(String.format(Locale.US, "%.1f", max));

        double temp;
        if(this.getNivel()== 0){
            temp = min;
            min = max;
            max = temp;
        }

        this.setIdeal(min, max);
    }

    private void setSaude(double imc) {
        Resources res = this.context.getResources();
        if (imc < 18.5) {

            this.saude = res.getString(R.string.grau_0);
            this.setNivel(0);
        } else if (imc > 40) {
            this.saude = res.getString(R.string.grau_5);
            this.setNivel(5);
        } else if (this.isBetween(imc, 18.6, 24.9)) {
            this.saude = res.getString(R.string.grau_1);
            this.setNivel(1);
        } else if (this.isBetween(imc, 25.0, 29.9)) {
            this.saude = res.getString(R.string.grau_2);
            this.setNivel(2);
        } else if (this.isBetween(imc, 30.0, 34.9)) {
            this.saude = res.getString(R.string.grau_3);
            this.setNivel(3);
        } else if (this.isBetween(imc, 35.0, 39.9)) {
            this.saude = res.getString(R.string.grau_4);
            this.setNivel(4);
        }
    }

    private void setImc(double imc) {
        this.imc = imc;
        this.setSaude(imc);
    }

    public void setGenero(char genero) {
        if (genero == 'm' || genero == 'f') {
            this.genero = genero;
        }
    }

    public void setAltura(int altura) {
        this.altura = altura;
        this.calcularImc();

    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public void setPeso(double peso) {
        this.peso = peso;
        this.calcularImc();
    }

    private void calcularImc() {
        if (this.altura != 0 && this.peso != 0.0) {
            double k = Math.pow(this.getAlturaM(), 2);
            k = Double.valueOf(String.format(Locale.US, "%.2f", k));



            if (k > 0) {
                double imc = this.getPeso() / k;
                imc = Double.valueOf(String.format(Locale.US, "%.2f", imc));
                this.setImc(imc);
                this.calcularIdeal();
            }

        }

    }

}
