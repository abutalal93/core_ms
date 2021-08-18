package restaurant.ms.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import restaurant.ms.core.dto.responses.FileRs;
import restaurant.ms.core.entities.FileDb;
import restaurant.ms.core.exceptions.HttpServiceException;
import restaurant.ms.core.repositories.FileDbRepo;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Locale;
import java.util.stream.Stream;

@Service
@Transactional
public class FileDbService {

    @Autowired
    private Environment environment;

    @Autowired
    private FileDbRepo fileDbRepo;

    public FileRs store(MultipartFile multipartFile, Locale locale) {
        try {

            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

            FileDb fileDB = new FileDb();

            fileDB.setName(multipartFile.getOriginalFilename());
            fileDB.setType(multipartFile.getContentType());
            fileDB.setData(multipartFile.getBytes());
            fileDB.setSize(multipartFile.getSize());
            fileDB = fileDbRepo.save(fileDB);
            fileDB.setUrl(environment.getProperty("file.upload-uri") + fileDB.getId());

            FileRs fileRs = new FileRs();
            fileRs.setName(fileDB.getName());
            fileRs.setType(fileDB.getType());
            fileRs.setUrl(fileDB.getUrl());
            fileRs.setSize(fileDB.getSize());

            return fileRs;
        } catch (IOException e) {
            throw new HttpServiceException(HttpStatus.BAD_REQUEST, "error_while_save_file", locale);
        }
    }


    public FileDb getFile(String id) {
        return fileDbRepo.findById(id).get();
    }

    public Stream<FileDb> getAllFiles() {
        return fileDbRepo.findAllBy().stream();
    }


}
