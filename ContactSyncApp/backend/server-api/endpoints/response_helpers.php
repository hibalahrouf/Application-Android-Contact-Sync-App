<?php

function send_json(bool $success, string $message, array $contacts = [], int $status = 200): void
{
    http_response_code($status);
    header('Content-Type: application/json; charset=utf-8');
    header('Access-Control-Allow-Origin: *');
    header('Access-Control-Allow-Headers: Content-Type');
    header('Access-Control-Allow-Methods: GET, POST, OPTIONS');

    echo json_encode([
        'success' => $success,
        'message' => $message,
        'contacts' => $contacts,
    ], JSON_UNESCAPED_UNICODE);
    exit;
}

function read_json_body(): array
{
    $body = file_get_contents('php://input');
    $payload = json_decode($body, true);

    return is_array($payload) ? $payload : $_POST;
}

if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    send_json(true, 'Preflight accepted');
}
