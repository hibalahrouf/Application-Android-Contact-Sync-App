<?php

require_once __DIR__ . '/../db/ConnectionProvider.php';

class AddressBookRepository
{
    private PDO $connection;

    public function __construct()
    {
        $this->connection = (new ConnectionProvider())->open();
    }

    public function create(string $personName, string $phoneNumber, string $source = 'android_device'): bool
    {
        $sql = 'INSERT INTO contacts (name, phone, source) VALUES (:name, :phone, :source)';
        $statement = $this->connection->prepare($sql);

        return $statement->execute([
            ':name' => $personName,
            ':phone' => $phoneNumber,
            ':source' => $source,
        ]);
    }

    public function all(): array
    {
        $sql = 'SELECT id, name, phone, source, created_at FROM contacts ORDER BY name ASC, phone ASC';
        $statement = $this->connection->prepare($sql);
        $statement->execute();

        return $statement->fetchAll();
    }

    public function find(string $needle): array
    {
        $sql = 'SELECT id, name, phone, source, created_at
                FROM contacts
                WHERE name LIKE :query OR phone LIKE :query
                ORDER BY name ASC, phone ASC';
        $statement = $this->connection->prepare($sql);
        $statement->execute([':query' => '%' . $needle . '%']);

        return $statement->fetchAll();
    }
}
