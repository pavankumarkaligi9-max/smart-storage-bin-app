package com.smartstoragebin.backend.service;

import com.smartstoragebin.backend.dto.*;
import java.util.List;

public interface DuplicateService {
    DuplicateCheckResponse checkForDuplicates(String hash);
    List<DuplicateGroupDTO> getAllDuplicateGroups();
    List<FileDTO> getDuplicatesByHash(String hash);
    FileDTO handleDuplicateAction(DuplicateActionRequest request);
}
