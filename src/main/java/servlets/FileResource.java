package servlets;

import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import dao.exceptions.DaoBadRequestException;
import dao.exceptions.DaoInternalServerException;
import dao.transaction.ITransactionFactory;
import dao.transaction.TransactionFactory;
import io.swagger.annotations.Api;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;  
import org.glassfish.jersey.media.multipart.FormDataParam;

import model.FileData;
import model.FileMeta;

@Api(value = "File")
@Path("/file")
public class FileResource {
    final static ITransactionFactory transactionFactory = TransactionFactory.getInstance();

    static final Set<String> allowedMediaTypes = Set.of(
        MediaType.APPLICATION_JSON,
        MediaType.TEXT_PLAIN,
        MediaType.APPLICATION_XML,
        "image/png", "image/jpg", "image/jpeg", "image/bmp",
        "application/pdf"
    );

    static final Set<String> allowedExtensions = Set.of(
        "json", "txt", "xml", "png", "jpg", "jpeg", "bmp", "pdf"
    );

    @POST
    @Path("/profile/{u_id}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public void saveProfilePicture(
            @FormDataParam("doc_id") int doc_id,
            @FormDataParam("file") final FormDataBodyPart body,
            @FormDataParam("file") final InputStream uploadedInputStream,
            @FormDataParam("file") final FormDataContentDisposition fileDetail,
            @PathParam("u_id") int userId
    ) throws IOException, DaoBadRequestException, DaoInternalServerException {
        System.out.println(uploadedInputStream);
        byte[] bytes;
        try(var out = new ByteArrayOutputStream(1024)){
            int read = 0;
            byte[] subBytes = new byte[1024];
            while((read = uploadedInputStream.read(subBytes)) != -1){
                System.out.println("writing");
                out.write(subBytes, 0, read);
            }
            out.flush();
            bytes = out.toByteArray();
        }catch(IOException e){
            throw e;
        }
        String mediaType = body.getMediaType().toString();
        var fileName = fileDetail.getFileName();
        var parts = fileName.split("\\.");
        if (parts.length < 1){
            throw new DaoBadRequestException("Missing File Extension " + " for file " + fileName);
        }
        var fileExtension = parts[parts.length-1].toLowerCase();
        if ( !allowedExtensions.contains(fileExtension)) {
            throw new DaoBadRequestException(
                    "Unsupported File Extension " + fileExtension + " for file " + fileName +
                            " should be " + allowedExtensions.stream().collect(Collectors.joining(", "))
            );
        }

        if (!allowedMediaTypes.contains(mediaType)) {
            throw new DaoBadRequestException(
                    "Unsupported File Media Type " + mediaType + " for file " + fileName +
                            " should be " + allowedMediaTypes.stream().collect(Collectors.joining(", "))
            );
        }

        var file = new FileData();
        file.setDoc_id(doc_id);
        file.setFile_name(fileName);
        file.setFile_type(body.getMediaType().toString());
        file.setFile_data(bytes);
        try (var transaction = transactionFactory.create()) {
            int fileId = transaction.fileDao().save(file);
            transaction.userDao().saveProfilePicture(userId, fileId);
        }
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public void saveFileToServer(
        @FormDataParam("doc_id") int doc_id,
        @FormDataParam("file") final FormDataBodyPart body,
        @FormDataParam("file") final InputStream uploadedInputStream, 
        @FormDataParam("file") final FormDataContentDisposition fileDetail
    )
            throws IOException, DaoInternalServerException, DaoBadRequestException
    {
        byte[] bytes;
        try(var out = new ByteArrayOutputStream(1024)){
            int read = 0;
            byte[] subBytes = new byte[1024];
            while((read = uploadedInputStream.read(subBytes)) != -1){
                out.write(subBytes, 0, read);
            }
            out.flush();
            bytes = out.toByteArray();
        }catch(IOException e){
            throw e;
        }
        String mediaType = body.getMediaType().toString();
        var fileName = fileDetail.getFileName();
        var parts = fileName.split("\\.");
        if (parts.length < 1){
            throw new DaoBadRequestException("Missing File Extension " + " for file " + fileName);
        }
        var fileExtension = parts[parts.length-1].toLowerCase();
        if ( !allowedExtensions.contains(fileExtension)) {
            throw new DaoBadRequestException(
                    "Unsupported File Extension " + fileExtension + " for file " + fileName +
                            " should be " + allowedExtensions.stream().collect(Collectors.joining(", "))
            );
        }

        if (!allowedMediaTypes.contains(mediaType)) {
            throw new DaoBadRequestException(
                    "Unsupported File Media Type " + mediaType + " for file " + fileName +
                            " should be " + allowedMediaTypes.stream().collect(Collectors.joining(", "))
            );
        }

        var file = new FileData();
        file.setDoc_id(doc_id);
        file.setFile_name(fileName);
        file.setFile_type(body.getMediaType().toString());
        file.setFile_data(bytes);
        try (var transaction = transactionFactory.create()) {
            transaction.fileDao().save(file);
        }
    }
    @GET
    @Path("file_id/{file_id}")
    public Response getFile(
        @PathParam("file_id") int file_id
    )
            throws DaoInternalServerException
    {
        FileData file = null;
        try (var transaction = transactionFactory.create()) {
            file = transaction.fileDao().get(file_id);
        }

        if (file == null){
            return Response.status(Status.NOT_FOUND).build();
        }
        String mediaType = file.getFile_type();
        if (!allowedMediaTypes.contains(mediaType)){
            mediaType = MediaType.TEXT_PLAIN;
        }
        String filename;
        try {
            filename = URLEncoder.encode(file.getFile_name(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return Response.serverError()
                .entity("Server does not support utf-8 encoding")
                .build();
        }
        String disposition = "inline; filename*=UTF-8''" + filename;

        return Response
            .ok(file.getFile_data(), mediaType)
            .header("Content-Disposition", disposition)
            .build();
    }

    @GET
    @Path("id/doc_id/{doc_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Integer> fileByDoc_id(
        @PathParam("doc_id") int doc_id
    )
            throws DaoInternalServerException
    {
        try (var transaction = transactionFactory.create()) {
            return transaction.fileDao().getIdsByDoc_id(doc_id);
        }
    }

    @GET
    @Path("meta/doc_id/{doc_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<FileMeta> metaByDoc_id(
        @PathParam("doc_id") int doc_id
    )
            throws DaoInternalServerException
    {
        try (var transaction = transactionFactory.create()) {
            return transaction.fileDao().getMetaByDoc_id(doc_id);
        }
    }
}
