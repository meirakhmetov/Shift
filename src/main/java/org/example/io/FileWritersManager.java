package org.example.io;

import org.example.exception.AppException;

import java.io.*;
import java.nio.file.*;

public class FileWritersManager {
    private final String outputDir, prefix;
    private final boolean append;
    private BufferedWriter intW, fltW, strW;

    public FileWritersManager(String outputDir, String prefix, boolean append) {
        this.outputDir = outputDir;
        this.prefix = prefix != null ? prefix : "";
        this.append = append;
        try {
            Files.createDirectories(Path.of(outputDir));
        } catch (IOException e) {
            throw new AppException("не могу создать папку %s".formatted(outputDir), e);
        }
    }

    public void writeInteger(String s) { write(s, () -> intW  = open(intW, "%sintegers.txt".formatted(prefix))); }
    public void writeFloat(String s) { write(s, () -> fltW = open(fltW, "%sfloats.txt".formatted(prefix)));   }
    public void writeString(String s) { write(s, () -> strW = open(strW, "%sstrings.txt".formatted(prefix)));  }

    private void write(String s, WriterOp open) {
        try {
            BufferedWriter bufferedWriter = open.open();
            bufferedWriter.write(s);
            bufferedWriter.newLine();
        } catch (IOException ex) {
            throw new AppException("ошибка записи", ex);
        }
    }

    private BufferedWriter open(BufferedWriter bufferedWriter, String name) throws IOException {
        if (bufferedWriter == null) {
            Path p = Path.of(outputDir, name);
            bufferedWriter = Files.newBufferedWriter(p,
                    StandardOpenOption.CREATE,
                    append ? StandardOpenOption.APPEND : StandardOpenOption.TRUNCATE_EXISTING);
        }
        return bufferedWriter;
    }

    public void closeAll() {
        IOException err = null;
        for (BufferedWriter bufferedWriter : new BufferedWriter[]{intW, fltW, strW}) {
            if (bufferedWriter == null) continue;
            try { bufferedWriter.close(); }
            catch (IOException e) { if (err == null) err = e; }
        }
        if (err != null) {
            throw new AppException("ошибка закрытия файлов", err);
        }
    }

    @FunctionalInterface
    private interface WriterOp {
        BufferedWriter open() throws IOException;
    }
}
