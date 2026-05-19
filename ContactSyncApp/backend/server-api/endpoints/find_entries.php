<?php

require_once __DIR__ . '/../repository/AddressBookRepository.php';
require_once __DIR__ . '/response_helpers.php';

try {
    $query = trim($_GET['q'] ?? '');

    if ($query === '') {
        send_json(false, 'Search text is required.', [], 422);
    }

    $repository = new AddressBookRepository();
    send_json(true, 'Search results loaded.', $repository->find($query));
} catch (Throwable $error) {
    send_json(false, 'Server error: ' . $error->getMessage(), [], 500);
}
