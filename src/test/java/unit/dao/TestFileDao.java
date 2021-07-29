package unit.dao;

import dao.contracts.IFileDao;
import dao.transaction.ITransaction;
import model.FileData;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

public class TestFileDao extends BaseDaoTest {
    Random rand = new Random();
    FileData randomFile(int doc_id){
        var file = new FileData();

        file.setDoc_id(doc_id);
        String name = Double.toString(Math.random());
        file.setFile_name(name);
        file.setFile_type("text/plain");
        var data = new byte[32];
        rand.nextBytes(data);
        file.setFile_data(data);

        return file;
    }

    IFileDao fileDao;
    @Override
    protected void withTransaction(ITransaction transaction) {
        fileDao = transaction.fileDao();
    }
    @Test
    void saveThenGet_returnsSame(){
        var expected = randomFile(1);
        int f_id = assertDoesNotThrow(()->fileDao.save(expected));
        expected.setFile_id(f_id);
        var actual = assertDoesNotThrow(()->fileDao.get(f_id));
        assertEquals(expected, actual);
    }
    @Test
    void saveThenGetIds_IncludesNewId(){
        var file = randomFile(1);
        int expected = assertDoesNotThrow(()->fileDao.save(file));
        var actualList = assertDoesNotThrow(() -> fileDao.getIdsByDoc_id(1));
        assertTrue(actualList.contains(expected));
    }
    @Test
    void saveThenGetMeta_IncludesSavedMeta(){
        var expected = randomFile(1);
        int f_id = assertDoesNotThrow(()->fileDao.save(expected));
        expected.setFile_id(f_id);
        var actualList = assertDoesNotThrow(() -> fileDao.getMetaByDoc_id(expected.getDoc_id()));
        var actualOpt = actualList.stream().filter((fileMeta)->fileMeta.getFile_id() == f_id).findAny();
        assertTrue(actualOpt.isPresent());
        var actual = actualOpt.get();
        assertEquals(expected.getDoc_id(), actual.getDoc_id());
        assertEquals(expected.getFile_name(), actual.getFile_name());
        assertEquals(expected.getFile_type(), actual.getFile_type());
    }
}
