package org.dbilik.fileServer.service.impl;

import org.dbilik.fileServer.service.DownloadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
public class DownloadServiceImpl implements DownloadService {

    private static final Logger log = LoggerFactory.getLogger(DownloadServiceImpl.class);

    @Override
    public void download(String id, HttpServletResponse response) {

    }

}
