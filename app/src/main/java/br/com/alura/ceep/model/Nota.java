package br.com.alura.ceep.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Nota implements Parcelable {

    private final String titulo;
    private final String descricao;

    public Nota(String titulo, String descricao) {
        this.titulo = titulo;
        this.descricao = descricao;
    }

    private Nota (Parcel from) {
        titulo = from.readString();
        descricao = from.readString();
    }

    public static final Parcelable.Creator<Nota>
            CREATOR = new Parcelable.Creator<Nota>() {

        public Nota createFromParcel(Parcel in) {
            return new Nota(in);
        }

        public Nota[] newArray(int size) {
            return new Nota[size];
        }
    };

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(titulo);
        dest.writeString(descricao);
    }
}