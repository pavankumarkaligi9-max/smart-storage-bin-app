package com.smartstoragebin.backend.service;

import com.smartstoragebin.backend.dto.*;
import java.util.List;

public interface BinService {
    List<BinFileDTO> getBinFiles();
    FileDTO restoreFromBin(Long id);
    void deletePermanently(Long id);
    void emptyBin();
}
