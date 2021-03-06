package com.airhacks.airpad.business.notes.entity;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Objects;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author adam-bien.com
 */
public class Note implements Externalizable {

    private StringProperty title;
    private StringProperty content;
    private String beforeImage;

    public Note() {
        this.title = new SimpleStringProperty();
        this.content = new SimpleStringProperty();
        this.content.set("");
        this.beforeImage = "";
    }

    public Note(String title) {
        this();
        this.title.set(title);
    }

    public StringProperty titleProperty() {
        return this.title;
    }

    public StringProperty contentProperty() {
        return this.content;
    }

    public String getIdentifier() {
        return titleProperty().get();
    }

    public boolean matches(String newValue) {
        if (newValue.trim().isEmpty()) {
            return true;
        }
        String titleValue = title.get();
        String contentValue = content.get();
        return (titleValue.contains(newValue) || contentValue.contains(newValue));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.getFingerprint());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Note other = (Note) obj;
        if (!Objects.equals(this.getFingerprint(), other.getFingerprint())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return title.get() + ":" + content.get();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(title.get());
        out.writeUTF(content.get());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        String titleValue = in.readUTF();
        String contentValue = in.readUTF();
        this.title.set(titleValue);
        this.content.set(contentValue);
    }

    public void synced() {
        this.beforeImage = getFingerprint();
    }

    public boolean isDirty() {
        return (!this.beforeImage.equals(getFingerprint()));
    }

    public String getFingerprint() {
        return toBindableString().get();
    }

    public StringExpression toBindableString() {
        return this.title.concat(":::").concat(this.content);
    }

    public void from(Note newNote) {
        this.content.set(newNote.content.get());
        this.title.set(newNote.title.get());
    }
}
