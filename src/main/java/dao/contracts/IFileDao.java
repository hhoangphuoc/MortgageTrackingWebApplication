package dao.contracts;

import java.util.List;

import dao.exceptions.DaoInternalServerException;
import model.FileData;
import model.FileMeta;

public interface IFileDao {
    int save(FileData file) throws DaoInternalServerException;
    FileData get(int file_id) throws DaoInternalServerException;;
    List<Integer> getIdsByDoc_id(int doc_id) throws DaoInternalServerException;
    List<FileMeta> getMetaByDoc_id(int doc_id) throws DaoInternalServerException;
}
