<?php

require_once __DIR__ . '/../repository/AddressBookRepository.php';
require_once __DIR__ . '/response_helpers.php';

try {
    $repository = new AddressBookRepository();
    send_json(true, 'Contacts loaded.', $repository->all());
} catch (Throwable $error) {
    send_json(false, 'Server error: ' . $error->getMessage(), [], 500);
}
