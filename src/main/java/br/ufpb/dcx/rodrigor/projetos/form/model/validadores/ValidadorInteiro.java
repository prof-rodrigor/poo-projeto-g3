package br.ufpb.dcx.rodrigor.projetos.form.model.validadores;

import br.ufpb.dcx.rodrigor.projetos.form.model.ResultadoValidacao;
import br.ufpb.dcx.rodrigor.projetos.form.model.ValidadorCampo;

public class ValidadorInteiro implements ValidadorCampo {
    @Override
    public ResultadoValidacao validarCampo(String valor) {
        if (valor == null || valor.isEmpty()) {
            return new ResultadoValidacao("O campo não pode ser vazio");
        }


        String inteiroRegex = "-?\\d+";
        if (!valor.matches(inteiroRegex)) {
            return new ResultadoValidacao("Informe um valor válido");
        }
        return new ResultadoValidacao();
    }
}
