<?php

require_once __DIR__ . '/../repository/AddressBookRepository.php';
require_once __DIR__ . '/response_helpers.php';

try {
    $payload = read_json_body();
    $name = trim($payload['name'] ?? '');
    $phone = trim($payload['phone'] ?? '');
    $source = trim($payload['source'] ?? 'android_device');

    if ($name === '' || $phone === '') {
        send_json(false, 'Name and phone are required.', [], 422);
    }

    $repository = new AddressBookRepository();
    $repository->create($name, $phone, $source === '' ? 'android_device' : $source);

    send_json(true, 'Contact saved.');
} catch (Throwable $error) {
    send_json(false, 'Server error: ' . $error->getMessage(), [], 500);
}
