 package dao;

 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.util.ArrayList;
 import java.util.List;

 import dao.contracts.IFileDao;

 import dao.exceptions.DaoInternalServerException;
 import dao.transaction.Transaction;
 import model.FileData;
 import model.FileMeta;

 public class FileDao extends BaseDao implements IFileDao {

     public FileDao(Transaction transaction) {
         super(transaction);
     }
     private final String GET_BY_FILE_ID_QUERY
             = "SELECT doc_id, file_name, file_type, file_data\n" +
             "FROM file\n" +
             "WHERE file_id = ?\n";

     public FileData get(int file_id)
         throws DaoInternalServerException
     {
         try {
             var statement = connection.prepareStatement(GET_BY_FILE_ID_QUERY);
             statement.setInt(1, file_id);
             ResultSet resultSet = statement.executeQuery();
             if (resultSet.next()) {
                 var file = new FileData();
                 int i = 1;
                 file.setFile_id(file_id);
                 file.setDoc_id(resultSet.getInt(i++));
                 file.setFile_name(resultSet.getString(i++));
                 file.setFile_type(resultSet.getString(i++));
                 file.setFile_data(resultSet.getBytes(i++));
                 return file;
             } else {
                 return null;
             }
         } catch (SQLException e){
             throw new DaoInternalServerException(e);
         }
     }

     private final String INSERT_QUERY
             = "INSERT INTO file \n" +
             "(doc_id, file_name, file_type, file_data)\n" +
             "VALUES (?, ?, ?, ?)\n" +
             "RETURNING file_id\n";

     public int save(FileData file) throws DaoInternalServerException
     {
         try {
             var statement = connection.prepareStatement(INSERT_QUERY);
             int i = 1;
             statement.setInt(i++, file.getDoc_id());
             statement.setString(i++, file.getFile_name());
             statement.setString(i++, file.getFile_type());
             statement.setBytes(i++, file.getFile_data());
             ResultSet resultSet = statement.executeQuery();
             if (resultSet.next()) {
                 return resultSet.getInt(1);
             } else {
                 throw new DaoInternalServerException("Failed to insert file");
             }
         } catch (SQLException e){
             throw new DaoInternalServerException(e);
         }
     }

     @Override
     public List<Integer> getIdsByDoc_id(int doc_id) throws DaoInternalServerException {
         try {
             var statement = connection.prepareStatement(
                     "SELECT file_id FROM file WHERE doc_id = ?"
             );
             statement.setInt(1, doc_id);
             var result = statement.executeQuery();
             List<Integer> ids = new ArrayList<>();
             while (result.next()){
                 ids.add(result.getInt(1));
             }
             return ids;
         } catch (SQLException e){
             throw new DaoInternalServerException(e);
         }
     }


     @Override
     public List<FileMeta> getMetaByDoc_id(int doc_id) throws DaoInternalServerException {
         try {
             var statement = connection.prepareStatement(
                     "SELECT file_id, file_name, file_type\n" +
                             "FROM file WHERE doc_id = ?"
             );
             statement.setInt(1, doc_id);
             var result = statement.executeQuery();
             List<FileMeta> metaList = new ArrayList<>();
             while (result.next()){
                 int i = 1;
                 var meta = new FileMeta();
                 meta.setDoc_id(doc_id);
                 meta.setFile_id(result.getInt(i++));
                 meta.setFile_name(result.getString(i++));
                 meta.setFile_type(result.getString(i++));
                 metaList.add(meta);
             }
             return metaList;
         } catch (SQLException e){
             throw new DaoInternalServerException(e);
         }
     }
 }
