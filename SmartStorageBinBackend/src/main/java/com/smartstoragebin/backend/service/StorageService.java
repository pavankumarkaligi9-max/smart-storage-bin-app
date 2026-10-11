package com.smartstoragebin.backend.service;

import com.smartstoragebin.backend.dto.*;

public interface StorageService {
    StorageResponse getStorageInfo();
    TemporaryDashboardResponse getTemporaryDashboard();
}
