package org.dbilik.fileServer.domain.database;

/**
 * Holds information about numbers of modifications, download and so on... about file
 * Keeps just one record per File and always update
 * No optimistic locking - do it asynchronously, maybe create 1 shared Thread for writing this info tasks...
 * in that way, we can also keep an order
 */
public class FileDemand {
}
